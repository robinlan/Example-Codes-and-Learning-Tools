% This demo program was developed Hsiao-Lung Chan, Ph.D. September 28, 2009
% Effect of windowing

clear
fs=500;
t=0:1/fs:0.3;
x = cos(2*pi*25*t) + cos(2*pi*35*t+pi/10);

subplot(2,2,1);
plot(t,x)
title('x(n)')
axis([min(t) max(t) -3 3])
xlabel('Time, s')
ylabel('x(n)')
title('Two sinusoids with a rectangular window')

Xf=fft(x);
resolution=fs/length(Xf);
f=(0:length(Xf)-1)*resolution;
Xf_power = Xf.*conj(Xf);  % power spectral density
index=1:length(Xf)/4;
subplot(2,2,2);
stem(f(index),Xf_power(index))
xlabel('Frequency, Hz')
ylabel('Power spectral density')
title('Spectrum')
axis([min(f(index)) max(f(index)) 0 max(Xf_power)*1.1])


% Using Hanning window
x=x.*hanning(length(x))';
subplot(2,2,3);
plot(t,x)
axis([min(t) max(t) -3 3])
title('x(n)')
ylabel('x(n)')
xlabel('Time, s')
title('Two sinusoids with a Hanning window')

Xf=fft(x);
resolution=fs/length(Xf);
f=(0:length(Xf)-1)*resolution;
Xf_power = Xf.*conj(Xf);  % power spectral density
index=1:length(Xf)/4;
subplot(2,2,4);
stem(f(index),Xf_power(index))
xlabel('Frequency, Hz')
ylabel('Power spectral density')
title('Spectrum')
axis([min(f(index)) max(f(index)) 0 max(Xf_power)*1.1])