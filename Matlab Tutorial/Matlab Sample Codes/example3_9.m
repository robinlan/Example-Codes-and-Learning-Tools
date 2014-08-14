clear
randn('state',0);
fs = 1000;                         % Sampling frequency
t = (0:fs)/fs;                     % One second worth of samples
xn = sin(2*pi*150*t) + 2*sin(2*pi*140*t) + 0.1*randn(size(t));

[P,F] = pmtm(xn,4,1024,fs);
Pow = (fs/1024) * sum(P)
subplot(211)
plot(F,10*log10(P))                % Plot in dB/Hz
xlabel('Frequency (Hz)');
ylabel('Power Spectral Density (dB/Hz)');

[P1,f] = pmtm(xn,3/2,1024,fs);
Pow1 = (fs/1024) * sum(P1)
subplot(212)
plot(f,10*log10(P1))               % Plot in dB/Hz
xlabel('Frequency (Hz)');
ylabel('Power Spectral Density (dB/Hz)');
