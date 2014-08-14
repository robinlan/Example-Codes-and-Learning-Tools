clear
fs = 100;
t = 0:1/fs:1;
x = sin(2*pi*t*3)+.25*sin(2*pi*t*40);
b = ones(1,10)/10;                    % 10 point averaging filter
y = filtfilt(b,1,x);                  % Noncausal filtering
yy = filter(b,1,x);                   % Normal filtering
plot(t,x,'linewidth',2)
hold on
plot(t,y,'--')
plot(t,yy,':')
xlabel('Time')
ylabel('Amplitude')
title('Anti-Causal, Zero-Phase Filter Implementation')
legend('x           (Original signal)','y   filtfilt (Noncausal filtering)','yy  filter (Normal filtering)')
axis([0 1 -2 2])
hold off
