clear
t = (0:1/1023:1);
x = sin(2*pi*60*t);
y = hilbert(x);
plot(t(1:50),real(y(1:50)))
hold on
plot(t(1:50),imag(y(1:50)),':')
hold off
legend('Original Signal','Hilbert Transform')
axis([0  0.05 -1.5  1.5])