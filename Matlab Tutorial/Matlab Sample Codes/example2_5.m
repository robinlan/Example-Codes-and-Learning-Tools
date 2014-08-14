clear
[z,p,k] = ellipap(5,0.5,20);
w = logspace(-1,1,1000);
h = freqs(k*poly(z),poly(p),w);
semilogx(w,abs(h))
grid
xlabel('Frequency (rad/sec)')
ylabel('Magnitude')
title('Magnitude Response')
