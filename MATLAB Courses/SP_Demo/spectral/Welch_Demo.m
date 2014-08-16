% This demo program was developed Hsiao-Lung Chan, Ph.D. September 28, 2009
% Averaged periodogram

clear

fs=5000;
t=0:1/fs:1;
x = sin(2*pi*200*t) + 2*sin(2*pi*350*t) + randn(size(t));
subplot(2,1,1)
index=1:500;
plot(t(index),x(index))
ylabel('x(n)')
xlabel('Time, s')
title('Two sinusoids + random noises')
axis([min(t(index)) max(t(index)) min(x(index))*1.1 max(x(index))*1.1])

Xf=fft(x);
resolution=fs/length(Xf);
f=(0:length(Xf)-1)*resolution;
Xf_power = Xf.*conj(Xf);  % power spectral density
subplot(2,2,3)
Xf_dB=10*log10(Xf_power);
index=1:length(Xf_power)/2;
plot(f(index),Xf_dB(index))
grid
xlabel('Frequency, Hz')
ylabel('Magnitude, dB')
title('Periodogram using full data length')
axis([min(f(index)) max(f(index)) min(Xf_dB(index)) max(Xf_dB(index))])

% Welch periodogram
NFFT=512;  % length of window
[P1,f] = pwelch(x,hanning(NFFT),NFFT/2,NFFT);  % PWELCH(X,WINDOW,NOVERLAP,NFFT)
f=f/pi*fs/2;
subplot(2,2,4)
P1_dB=10*log10(P1);
plot(f,P1_dB)
grid
ylabel('Magnitude, dB');
xlabel('Frequency, Hz');
title('Welch periodogram')
axis([min(f) max(f) min(P1_dB) max(P1_dB)]);