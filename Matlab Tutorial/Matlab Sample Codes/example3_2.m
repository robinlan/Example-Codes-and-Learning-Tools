clear
randn('state',0);
fs = 1000;                         % Sampling frequency
t = (0:fs)/fs;                     % One second worth of samples
xn = sin(2*pi*150*t) + 2*sin(2*pi*140*t) + 0.1*randn(size(t));
Pxx = periodogram(xn,[],'twosided',1024,fs);
periodogram(xn,[],'twosided',1024,fs);

Pow = (fs/length(Pxx)) * sum(Pxx)

Pxxo = periodogram(xn,[],1024,fs);
Pow = (fs/(2*length(Pxxo))) * sum(Pxxo)