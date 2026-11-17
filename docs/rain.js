/* chuva matrix de fundo — compartilhada entre a landing e o simulador */
(function () {
  'use strict';
  const $ = (s) => document.querySelector(s);
  const cv = $('#rain'), ctx = cv.getContext('2d');
  const reduz = window.matchMedia('(prefers-reduced-motion: reduce)').matches;
  const CH = 'アイウエオカキクケコサシスセソタチツテトナニヌネノ0123456789ADDSUBLWSWAVGREV';
  let cols = [], fs = 16, raf = null, last = 0;
  function resize() {
    cv.width = innerWidth; cv.height = innerHeight;
    cols = Array(Math.ceil(cv.width / fs)).fill(0).map(() => Math.random() * -50);
  }
  function frame(t) {
    raf = requestAnimationFrame(frame);
    if (t - last < 55) return;
    last = t;
    ctx.fillStyle = 'rgba(0,0,0,0.08)';
    ctx.fillRect(0, 0, cv.width, cv.height);
    ctx.fillStyle = '#00ff41';
    ctx.font = fs + 'px monospace';
    for (let i = 0; i < cols.length; i++) {
      const y = cols[i] * fs;
      if (y > 0) ctx.fillText(CH[(Math.random() * CH.length) | 0], i * fs, y);
      cols[i] = (y > cv.height && Math.random() > 0.975) ? 0 : cols[i] + 1;
    }
  }
  function chuva(on) {
    cancelAnimationFrame(raf); raf = null;
    ctx.clearRect(0, 0, cv.width, cv.height);
    if (on) raf = requestAnimationFrame(frame);
  }
  addEventListener('resize', resize);
  resize();
  chuva(!reduz);

})();
