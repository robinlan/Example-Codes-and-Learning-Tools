clear
randn('state',0);
fs = 1000;                         % Sampling frequency
t = (0:fs)/fs;                     % One second worth of samples
xn = sin(2*pi*150*t) + 2*sin(2*pi*140*t) + 0.1*randn(size(t));
subplot(211)
[P1,f] = pwelch(xn,hamming(256),128,1024,fs);
[P2,f] = pburg(xn,14,1024,fs);
plot(f,10*log10(P1),':',f,10*log10(P2)); grid
ylabel('PSD Estimates (dB/Hz)');
xlabel('Frequency (Hz)');
legend('Welch','Burg (14th order model)')
axis([0 200 -60 0])

subplot(212)
[P3,f] = pwelch(xn,hamming(256),128,1024,fs);
[P4,f] = pburg(xn,12,1024,fs);
plot(f,10*log10(P3),':',f,10*log10(P4)); grid
ylabel('PSD Estimates (dB/Hz)');
xlabel('Frequency (Hz)');
legend('Welch','Burg (12th order model)')
axis([0 200 -60 0])