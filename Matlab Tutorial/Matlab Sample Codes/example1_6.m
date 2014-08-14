clear
fs = 10000;               % 取樣速率
t = 0:1/fs:1.5;

subplot(2,1,1)
y1 = sawtooth(2*pi*50*t);
plot(t,y1,'linewidth',2)
axis([0 0.2 -2 2])
xlabel('Time')
ylabel('Amplitude')
title('鋸齒波')
grid on

subplot(2,1,2)
y2 = square(2*pi*50*t);
plot(t,y2,'linewidth',2)
axis([0 0.2 -2 2])
xlabel('Time')
ylabel('Amplitude')
title('方波')
grid on


