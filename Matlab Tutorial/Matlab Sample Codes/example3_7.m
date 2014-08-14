clear
randn('state',0);
fs = 1000;                         % Sampling frequency
t = (0:fs/10)/fs;                  % One-tenth of a second worth of samples
xn = sin(2*pi*150*t) + 2*sin(2*pi*140*t) + 0.1*randn(size(t));
subplot(211)
periodogram(xn,rectwin(length(xn)),1024,fs);
subplot(212)
periodogram(xn,hamming(length(xn)),1024,fs);