clear
t = (0:1/255:1);
x = sin(2*pi*120*t);
y = real(ifft(fft(x)));
