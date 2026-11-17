# 🧠 Simulador de Pipeline RISC

Simulador web de um pipeline RISC de 5 estágios (**IF → ID → EX → MEM → WB**), com motor escrito em Java.
Você escreve as instruções no navegador, executa e acompanha cada instrução atravessando o pipeline, ciclo a ciclo, numa interface estilo terminal.

## 🚀 Como rodar

Precisa só de **Docker** (ou Podman):

```bash
docker compose up -d --build
```

Abra **http://localhost:8080**.

```bash
docker compose logs -f   # ver logs
docker compose down      # parar
```

Sem compose:

```bash
docker build -t simulador-risc .
docker run -d -p 8080:8080 --name simulador-risc simulador-risc
```

> Depois de mudar o código, suba de novo com `--build`. No Podman, se a página continuar antiga, use `podman-compose build && podman-compose up -d --force-recreate`.

## 🖥️ Usando a interface

**Página inicial**: resumo dos 5 estágios, uma comparação RISC × CISC e um desenho de como o pipeline sobrepõe as instruções. Clique em **INICIAR SIMULAÇÃO**.

**Simulador**:

| Área | O que faz |
|---|---|
| **Entrada** | Abas para editar `instrucoes.txt`, `memoria.txt` e `registradores.txt`. O que você digita fica salvo no navegador. **RESTAURAR** volta para os arquivos originais. |
| **EXECUTAR** | Roda o simulador Java e anima os ciclos. Durante a animação vira **PAUSAR**; pausado no meio, vira **CONTINUAR**. Atalho: `Ctrl+Enter` no editor. |
| **◀ ▶ e vel** | Voltar ou avançar um ciclo (também pelas setas do teclado). O slider ajusta a velocidade da animação. |
| **Ciclo** | O PC atual, a instrução que está em cada estágio e o log daquele ciclo. |
| **Diagrama do pipeline** | Tabela instrução × ciclo. Clique num número de ciclo para ir direto a ele. |
| **Registradores / Memória** | Os 32 valores de cada um no ciclo atual; o que mudou fica destacado em amarelo. |
| **Console** | Saída completa do Java, com botões para baixar `simulacao_log.txt`, `registradores_resultado.txt` e `memoria_resultado.txt`. |

Erros de entrada (ex.: um número inválido em `memoria.txt`) aparecem no console exatamente como o Java os reporta.

### Formato das entradas

Instruções, uma por linha, com os operandos separados por espaço:

```
add x11 x9 x17
sub x2 x4 x9
lw x1 3(x6)
add x10 x1 x3
sw x10 3(x6)
swap x5 x8
avg x22 x27 x21
rev x14 x15
```

Memória e registradores: um inteiro por linha, até 32 valores cada.

| Instrução | Exemplo | Efeito |
|---|---|---|
| `add` | `add x1 x2 x3` | x1 = x2 + x3 |
| `sub` | `sub x1 x2 x3` | x1 = x2 − x3 |
| `lw` | `lw x1 3(x6)` | carrega da memória para x1 |
| `sw` | `sw x1 3(x6)` | grava x1 na memória |
| `swap` | `swap x1 x2` | troca x1 e x2 |
| `avg` | `avg x1 x2 x3` | x1 = (x2 + x3) / 2 |
| `rev` | `rev x1 x2` | x1 = x2 com a ordem dos bytes invertida |

## ⚙️ Como funciona

A interface **não simula nada**: quem executa é o motor Java de `src/`. A página só desenha o que ele produz.

```
navegador ──POST /api/simular──▶ Servidor.java ──processo novo──▶ WebMain.java
   ▲                                                                  │ grava src/input/*.txt
   │                                                                  │ chama Main.main()
   └──────── JSON: console + log + resultados ◀────────────────────────┘ lê src/output/*.txt
```

- `web/Servidor.java`: servidor HTTP feito só com o JDK, sem dependências. Serve o frontend (`docs/`) e a API.
- `web/WebMain.java`: grava as entradas onde o `Parser` lê, chama o `Main.main()` original e coleta o console e os arquivos de saída.
- Cada simulação roda num **processo Java separado**, numa pasta temporária própria. Assim, execuções simultâneas não se misturam, e uma execução travada é encerrada depois de 15 s.
- O frontend (`docs/app.js`) monta o diagrama e o estado de cada ciclo lendo as linhas `[IF]`, `[WB]` e `[MEM]` do `simulacao_log.txt`. Em seguida, confere o resultado final com os arquivos `*_resultado.txt` gerados pelo Java.

### API

`POST /api/simular` (`application/x-www-form-urlencoded`)

| Campo | Conteúdo |
|---|---|
| `instrucoes` | texto de `instrucoes.txt` |
| `memoria` | texto de `memoria.txt` |
| `registradores` | texto de `registradores.txt` |

Responde um JSON com `console.txt`, `simulacao_log.txt`, `registradores_resultado.txt`, `memoria_resultado.txt`, `status.txt`, `exitCode` e `ms` (e `erro`, se houver).

```bash
curl -X POST localhost:8080/api/simular \
  --data-urlencode "instrucoes@src/input/instrucoes.txt" \
  --data-urlencode "memoria@src/input/memoria.txt" \
  --data-urlencode "registradores@src/input/registradores.txt"
```

`GET /healthz` responde `ok`.

### Configuração (variáveis de ambiente)

| Variável | Padrão | Uso |
|---|---|---|
| `PORT` | `8080` | porta do servidor |
| `MAX_CONCORRENTES` | `4` | simulações rodando ao mesmo tempo |
| `STATIC_DIR` | `/app/docs` | pasta do frontend |

## 🧩 Estrutura do projeto

```
docs/                      frontend
├── index.html             página inicial
├── simulador.html         simulador
├── app.js                 lógica da interface (chama a API e desenha os ciclos)
├── rain.js                chuva "matrix" de fundo
└── style.css
web/
├── Servidor.java          servidor HTTP + API
└── WebMain.java           ponte entre a API e o Main.main()
src/                       motor do simulador (Java)
├── Main.java · Simulador.java · Parser.java · SalvarLOG.java
├── input/                 Instrucao, Memoria, Registradores + arquivos de entrada
├── stages/                PC, A_Buscar (IF), B_Decodificar (ID), C_Executar (EX),
│                          D_Acessar (MEM), E_Escrever (WB)
└── output/                saídas da última execução por linha de comando
Dockerfile · docker-compose.yml
```

### O motor

A cada ciclo, o `Simulador` chama os estágios **de trás para frente** (WB, MEM, EX, ID, IF). Assim cada estágio consome o que o anterior produziu no ciclo passado, o que imita os registradores de pipeline sendo atualizados ao mesmo tempo. Com `n` instruções, a simulação leva `n + 4` ciclos.

## ✅ Status

| Item | Status |
|---|---|
| Pipeline de 5 estágios | ✅ |
| `add`, `sub`, `lw`, `sw` | ✅ |
| `swap`, `avg`, `rev` | ✅ (ver limitações) |
| Entradas carregadas de arquivo, log e resultados salvos | ✅ |
| Interface web + Docker | ✅ |
| Tratamento de conflitos (stall / forwarding) | ❌ fora do escopo |

### ⚠️ Limitações conhecidas

- **Sem tratamento de conflitos de dados.** Uma instrução que depende da anterior lê o valor antigo. No exemplo, `add x10 x1 x3` lê `x1 = 12` no ciclo 5, mas o `lw x1` só grava `49` no ciclo 7, e o resultado sai `x10 = 19` em vez de `56`. Dá para ver isso no simulador. Com 2 instruções independentes entre as duas, o valor chega a tempo.
- **Endereço de `lw`/`sw`.** Em `lw x1 3(x6)`, o deslocamento `3` é tratado como registrador no estágio ID, e o endereço sai `x6 + x3` (88 + 7 = 95) em vez de `x6 + 3`.
- **`rev`.** Inverte a **ordem dos bytes**; a especificação pede a inversão dos **bits**.
- **Endereços acima de 31** dão a volta na memória (95 → 63 → 31).

## 💻 Sem Docker (linha de comando)

Com Java 17+, a partir da raiz do repositório:

```bash
javac -d out $(find src -name '*.java')
java -cp out Main
```

Lê `src/input/*.txt`, imprime os ciclos no console e grava os resultados em `src/output/`.

Para subir a interface web sem Docker:

```bash
javac -d out $(find src web -name '*.java')
java -cp out Servidor     # http://localhost:8080
```

---

<details>
<summary>📜 Especificação original</summary>

Simulador de pipeline em 5 estágios baseado no modelo de instrução RISC, seguindo as características gerais do modelo, o padrão de processamento e as diretrizes de cada estágio.

- O simulador deve ser feito em **Java**.
- O conteúdo da memória deve ser carregado a partir de um arquivo.
- O conteúdo dos registradores deve ser carregado a partir de um arquivo.
- As instruções devem ser carregadas a partir de um arquivo.
- Um **Parser** deve ser usado pelo simulador para inicializar a lista de instruções.

**Operadores principais**

- `ADD`: soma valores e salva em um registrador. Exemplo: `add r0 r1 r2`
- `SUB`: subtrai valores e salva em um registrador. Exemplo: `sub r0 r1 r2`
- `LW`: carrega o conteúdo de uma memória em um registrador. Exemplo: `lw rs2, offset(rs1)`
- `SW`: armazena o conteúdo do registrador em uma memória. Exemplo: `sw rs2, offset(rs1)`

**Operadores desafio**

- `SWAP`: troca os valores dos registradores. Exemplo: `swap r0 r1`
- `AVG`: calcula a média entre dois registradores. Exemplo: `avg r0 r1 r2`
- `REV`: reverte a ordem dos bits do registrador. Exemplo: `rev r0 r1`
  Considerando que são armazenados inteiros de 32 bits em cadeias de 8 bits cada.

  ```
  Entrada:
    00000000 00000000 00000000 00001001
    00000000 00000000 00000000 00001111
    00000000 00000000 00000000 10000000
    00000000 00000000 00000000 00000001
    11100000 00000000 00000000 00000000
  ```
  ```
  Saída:
    10010000 00000000 00000000 00000000
    11110000 00000000 00000000 00000000
    00000001 00000000 00000000 00000000
    10000000 00000000 00000000 00000000
    00000000 00000000 00000000 00000111
  ```

</details>
