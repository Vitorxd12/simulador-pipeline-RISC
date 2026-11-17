/*
 * app.js — interface. NÃO simula nada: envia as entradas para /api/simular, onde o servidor
 * roda o simulador Java original (src/, sem alterações), e só LÊ o que o Java produziu
 * (console, simulacao_log.txt, *_resultado.txt) para desenhar a visualização.
 */
(function () {
  'use strict';
  const $ = (s) => document.querySelector(s);
  const $$ = (s) => Array.from(document.querySelectorAll(s));
  const STAGES = ['IF', 'ID', 'EX', 'MEM', 'WB'];
  const NOMES = { IF: 'busca', ID: 'decodifica', EX: 'executa', MEM: 'memória', WB: 'escreve' };
  const TAM = 32;
  const API = 'api/simular'; // relativo: funciona também atrás de proxy em subpasta

  // conteúdo original de src/input/*.txt
  const PADRAO = {
    instrucoes: 'add x11 x9 x17\nsub x2 x4 x9\nlw x1 3(x6)\nadd x10 x1 x3\nsw x10 3(x6)\nswap x5 x8\navg x22 x27 x21\nrev x14 x15\n',
    memoria: '58\n21\n73\n2\n85\n90\n17\n33\n44\n96\n61\n12\n68\n39\n25\n88\n8\n77\n99\n47\n30\n62\n4\n100\n16\n53\n71\n35\n83\n7\n26\n49',
    registradores: '73\n12\n94\n7\n56\n35\n88\n2\n41\n67\n98\n19\n63\n26\n39\n84\n10\n91\n33\n59\n76\n20\n5\n48\n87\n71\n23\n100\n16\n44\n28\n60',
  };

  const LS = 'risc-pipeline-inputs';
  const ed = {
    instrucoes: $('#in-instrucoes'),
    memoria: $('#in-memoria'),
    registradores: $('#in-registradores'),
  };

  let R = null;      // modelo montado a partir das saídas do Java
  let cur = 0;       // 0 = estado inicial; 1..N = ciclo
  let timer = null;
  let ocupado = false;
  let sujo = false;   // entradas mudaram desde a última execução

  // ---------- entradas ----------
  function carregarEntradas() {
    let salvo = null;
    try { salvo = JSON.parse(localStorage.getItem(LS) || 'null'); } catch (_) {}
    for (const k in ed) ed[k].value = (salvo && typeof salvo[k] === 'string') ? salvo[k] : PADRAO[k];
  }
  function salvarEntradas() {
    try { localStorage.setItem(LS, JSON.stringify({ instrucoes: ed.instrucoes.value, memoria: ed.memoria.value, registradores: ed.registradores.value })); } catch (_) {}
  }
  $$('.tab').forEach((t) => t.addEventListener('click', () => {
    $$('.tab').forEach((x) => x.classList.toggle('on', x === t));
    for (const k in ed) ed[k].hidden = k !== t.dataset.tab;
  }));
  Object.values(ed).forEach((t) => t.addEventListener('input', () => { salvarEntradas(); sujo = true; parar(); }));

  // ---------- helpers ----------
  const esc = (s) => s.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
  function colorir(txt) {
    return txt.split('\n').map((l) => {
      const h = esc(l);
      if (/NULL_/.test(l)) return '<span class="null">' + h + '</span>';
      if (/ERROR|Erro|Exception|^\s+at /.test(l)) return '<span class="err">' + h + '</span>';
      if (/ajustado/.test(l)) return '<span class="warn">' + h + '</span>';
      if (/^-{5,}/.test(l)) return '<span class="sep">' + h + '</span>';
      return h;
    }).join('\n');
  }
  function status(msg, erro) {
    const s = $('#status');
    s.textContent = msg;
    s.classList.toggle('err', !!erro);
  }
  // Java faz "while (e >= len) e -= len" — mesmo resultado que o resto para e >= 0
  const ajusta = (e) => (e >= 0 ? e % TAM : e);

  // ---------- leitura das saídas do Java ----------
  function valoresDaSecao(linhas, titulo) {
    const i = linhas.findIndex((l) => l.includes(titulo));
    const v = new Array(TAM).fill(0);
    if (i < 0 || i + 2 >= linhas.length || /^-{5,}/.test(linhas[i + 2].trim())) return v;
    linhas[i + 2].trim().split(/\s+/).filter(Boolean).slice(0, TAM).forEach((x, k) => { v[k] = parseInt(x, 10) || 0; });
    return v;
  }

  function montarModelo(consoleTxt, logTxt, regTxt, memTxt, statusJava) {
    const cl = consoleTxt.split('\n');
    const instrucoes = [];
    cl.forEach((l) => {
      const m = l.match(/^Instrução\[(\d+)\] = (.*)$/);
      if (m) instrucoes.push({ idx: +m[1], texto: m[2].trim() });
    });
    const regInicial = valoresDaSecao(cl, 'Iniciando Registradores');
    const memInicial = valoresDaSecao(cl, 'Iniciando Memoria');

    // divide o log do Java em ciclos: cada ciclo começa no WB (primeiro estágio chamado no while)
    const ciclos = [];
    let cabecalho = '', rodape = '';
    let regs = regInicial.slice(), mem = memInicial.slice();
    let pipe = [null, null, null, null, null]; // IF ID EX MEM WB
    let buscadas = 0;
    let atual = null;
    const fechar = () => {
      if (!atual) return;
      atual.regs = regs.slice();
      atual.mem = mem.slice();
      ciclos.push(atual);
    };
    (logTxt || '').split('\n').forEach((l) => {
      if (/^\s*\[(NULL_)?WB\]/.test(l)) {
        fechar();
        // registradores entre estágios avançam (rescrever <- racessar <- ...)
        pipe = [null, pipe[0], pipe[1], pipe[2], pipe[3]];
        atual = { n: ciclos.length + 1, pc: ciclos.length * 4, log: '', estagios: {} };
        STAGES.forEach((s, k) => { atual.estagios[s] = pipe[k]; });
      }
      if (/Simulação concluída/.test(l)) { fechar(); atual = null; rodape += l + '\n'; return; }
      if (!atual) { if (l !== '' || cabecalho) cabecalho += l + '\n'; return; }
      atual.log += l + '\n';
      let m;
      if (/^\[IF\] Instrução buscada/.test(l)) {
        const ins = instrucoes[buscadas] || { idx: buscadas, texto: (l.match(/"(.*)"/) || [, '?'])[1] };
        buscadas++;
        pipe[0] = ins;
        atual.estagios.IF = ins;
      } else if ((m = l.match(/^\[WB\] Operação \S+ escrevendo valor da memoria \((-?\d+)\) no registrador R(\d+)\./))) {
        regs[ajusta(+m[2])] = +m[1];
      } else if ((m = l.match(/^\[WB\] Swap: trocou R(\d+) \((-?\d+)\) com R(\d+) \((-?\d+)\)/))) {
        regs[ajusta(+m[1])] = +m[4];
        regs[ajusta(+m[3])] = +m[2];
      } else if ((m = l.match(/^\[MEM\] Acessando memoria SW → Mem\[(-?\d+)\] = (-?\d+)/))) {
        mem[ajusta(+m[1])] = +m[2];
      }
    });
    fechar();
    if (ciclos.length && !rodape) ciclos[ciclos.length - 1].interrompido = true;

    const parseFinal = (t) => (t == null ? null : t.split('\n').filter((x) => x.trim() !== '').map(Number));
    const regFinal = parseFinal(regTxt), memFinal = parseFinal(memTxt);
    const ultimo = ciclos.length ? ciclos[ciclos.length - 1] : { regs: regInicial, mem: memInicial };
    const confere = (a, b) => !a || a.every((x, i) => x === b[i]);
    return {
      instrucoes, regInicial, memInicial, ciclos, cabecalho, rodape,
      console: consoleTxt, log: logTxt, regTxt, memTxt, statusJava,
      divergente: !confere(regFinal, ultimo.regs) || !confere(memFinal, ultimo.mem),
    };
  }

  // ---------- execução do Java (no servidor) ----------
  async function executar(animar) {
    if (ocupado) return;
    ocupado = true;
    rotulo();
    parar();
    salvarEntradas();
    $('#btn-main').disabled = $('#btn-reset').disabled = true;
    status('[ EXECUTANDO Main.main() EM JAVA... ]');
    try {
      const resp = await fetch(API, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ instrucoes: ed.instrucoes.value, memoria: ed.memoria.value, registradores: ed.registradores.value }),
      });
      let d;
      try { d = await resp.json(); } catch (_) { throw new Error('servidor respondeu HTTP ' + resp.status); }
      if (d['console.txt'] == null) throw new Error(d.erro || ('HTTP ' + resp.status));
      const con = d['console.txt'];
      R = montarModelo(con, d['simulacao_log.txt'], d['registradores_resultado.txt'], d['memoria_resultado.txt'], (d['status.txt'] || '').trim());
      const problema = d.exitCode !== '0' || R.statusJava !== 'OK' || /Erro|Exception/.test(con);
      if (d.erro) status('[ ' + d.erro.toUpperCase() + ' ]', true);
      else if (R.divergente) status('[ AVISO · visualização difere do resultado do Java — confie no console ]', true);
      else if (problema) status('[ JAVA REPORTOU ERRO · veja o console ]', true);
      else status('[ OK · Java · ' + R.instrucoes.length + ' instr · ' + R.ciclos.length + ' ciclos · ' + d.ms + ' ms ]');
      $('#cyc-total').textContent = R.ciclos.length;
      $('#console').innerHTML = colorir(con);
      sujo = false;
      montarDiagrama();
      ir(0, false);
      if (animar) setTimeout(play, 250);
    } catch (e) {
      status('[ FALHA: ' + (e && e.message ? e.message : e) + ' ]', true);
    } finally {
      ocupado = false;
      $('#btn-main').disabled = $('#btn-reset').disabled = false;
      rotulo();
    }
  }

  // ---------- visualização ----------
  function montarDiagrama() {
    const t = $('#diagram');
    const N = R.ciclos.length;
    let h = '<thead><tr><th>instr \\ ciclo</th><th data-c="0" tabindex="0">0</th>';
    for (let c = 1; c <= N; c++) h += '<th data-c="' + c + '" tabindex="0">' + c + '</th>';
    h += '</tr></thead><tbody>';
    R.instrucoes.forEach((ins) => {
      h += '<tr><th title="' + esc(ins.texto) + '">' + esc(ins.idx + ': ' + ins.texto) + '</th><td data-c="0"></td>';
      for (let c = 1; c <= N; c++) {
        const est = R.ciclos[c - 1].estagios;
        const st = STAGES.find((s) => est[s] && est[s].idx === ins.idx);
        h += '<td data-c="' + c + '"' + (st ? ' class="st" title="' + st + '"' : '') + '>'
          + (st ? '<span class="full">' + st + '</span><span class="abbr">' + st[0] + '</span>' : '') + '</td>';
      }
      h += '</tr>';
    });
    t.innerHTML = h + '</tbody>';
    t.querySelectorAll('thead th[data-c]').forEach((th) => {
      const go = () => { parar(); ir(+th.dataset.c, true); };
      th.addEventListener('click', go);
      th.addEventListener('keydown', (e) => { if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); go(); } });
    });
  }

  function estado(c) {
    if (c === 0) return { regs: R.regInicial, mem: R.memInicial };
    return R.ciclos[c - 1];
  }

  function celulas(el, prefixo, atual, anterior, flash) {
    let h = '';
    for (let i = 0; i < atual.length; i++) {
      const mudou = anterior && atual[i] !== anterior[i];
      h += '<div class="cell' + (mudou ? ' chg' + (flash ? ' flash' : '') : '') + '" title="' + prefixo + i + ' = ' + atual[i] + '">'
        + '<div class="k">' + prefixo + i + '</div><div class="v' + (String(atual[i]).length > 5 ? ' long' : '') + '">' + atual[i] + '</div></div>';
    }
    el.innerHTML = h;
  }

  function ir(c, flash) {
    if (!R) return;
    const N = R.ciclos.length;
    cur = Math.max(0, Math.min(N, c));
    const ciclo = cur > 0 ? R.ciclos[cur - 1] : null;

    $('#cyc-n').textContent = cur;
    $('#cyc-pc').textContent = ciclo ? ciclo.pc : 0;

    $('#stages').innerHTML = STAGES.map((s) => {
      const x = ciclo && ciclo.estagios[s];
      return '<div class="stage ' + (x ? 'busy' : 'empty') + '"><div class="lbl">' + s + '</div>'
        + '<div class="ins">' + (x ? esc(x.texto) : '— ' + NOMES[s]) + '</div></div>';
    }).join('');

    let log;
    if (cur === 0) log = (R.cabecalho || '') + '> estado inicial carregado. avance os ciclos.';
    else log = ciclo.log + (cur === N ? R.rodape : '') + (ciclo.interrompido ? '> simulação interrompida — veja o console.' : '');
    $('#cyc-log').innerHTML = colorir(log.replace(/\n+$/, ''));

    const a = estado(cur), p = cur > 0 ? estado(cur - 1) : null;
    celulas($('#regs'), 'x', a.regs, p && p.regs, flash);
    celulas($('#mem'), 'm', a.mem, p && p.mem, flash);

    $$('#diagram [data-c]').forEach((el) => el.classList.toggle('cur', +el.dataset.c === cur));
    const th = $('#diagram thead th[data-c="' + cur + '"]');
    if (th && flash) th.scrollIntoView({ block: 'nearest', inline: 'center', behavior: 'smooth' });

    $('#btn-prev').disabled = cur === 0;
    $('#btn-next').disabled = cur === N;
    rotulo();
  }

  // ---------- controles ----------
  // botão único: EXECUTAR (roda o Java e anima) / PAUSAR / CONTINUAR
  function rotulo() {
    const b = $('#btn-main');
    if (ocupado) b.textContent = 'EXECUTANDO...';
    else if (timer) b.textContent = 'PAUSAR';
    else if (R && !sujo && cur > 0 && cur < R.ciclos.length) b.textContent = 'CONTINUAR';
    else b.textContent = 'EXECUTAR';
  }
  function principal() {
    if (ocupado) return;
    if (timer) return parar();
    if (R && !sujo && cur > 0 && cur < R.ciclos.length) return play();
    executar(true);
  }
  function parar() {
    if (timer) clearInterval(timer);
    timer = null;
    rotulo();
  }
  function play() {
    if (!R || timer) return;
    if (cur >= R.ciclos.length) ir(0, false);
    timer = setInterval(() => {
      if (cur >= R.ciclos.length) return parar();
      ir(cur + 1, true);
    }, 1650 - $('#speed').value); // slider: direita = mais rápido
    rotulo();
  }
  $('#speed').addEventListener('input', () => { if (timer) { parar(); play(); } });
  $('#btn-main').addEventListener('click', principal);
  $('#btn-reset').addEventListener('click', () => {
    parar();
    for (const k in ed) ed[k].value = PADRAO[k];
    salvarEntradas();
    sujo = true;
    rotulo();
  });
  $('#btn-prev').addEventListener('click', () => { parar(); ir(cur - 1, true); });
  $('#btn-next').addEventListener('click', () => { parar(); ir(cur + 1, true); });
  document.addEventListener('keydown', (e) => {
    if (e.target.tagName === 'TEXTAREA' || e.target.tagName === 'INPUT') {
      if (e.key === 'Enter' && (e.ctrlKey || e.metaKey)) { e.preventDefault(); parar(); executar(true); }
      return;
    }
    if (e.key === 'ArrowRight') { parar(); ir(cur + 1, true); }
    else if (e.key === 'ArrowLeft') { parar(); ir(cur - 1, true); }
  });

  // ---------- downloads (arquivos gerados pelo Java) ----------
  $$('[data-dl]').forEach((b) => b.addEventListener('click', () => {
    if (!R) return;
    const nome = b.dataset.dl;
    const conteudo = nome === 'simulacao_log.txt' ? R.log : nome === 'registradores_resultado.txt' ? R.regTxt : R.memTxt;
    if (conteudo == null) { status('[ o Java não gerou ' + nome + ' ]', true); return; }
    const url = URL.createObjectURL(new Blob([conteudo], { type: 'text/plain;charset=utf-8' }));
    const a = document.createElement('a');
    a.href = url; a.download = nome; document.body.appendChild(a); a.click(); a.remove();
    setTimeout(() => URL.revokeObjectURL(url), 1000);
  }));

  // ---------- boot ----------
  $('#status').classList.add('cursor');
  carregarEntradas();
  executar(false);
})();
