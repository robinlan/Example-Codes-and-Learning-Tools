% This demo program was developed Hsiao-Lung Chan, Ph.D. September 28, 2009
% Effect of windowing

clear
fs=500;
t=0:1/fs:0.3;
x = 10+cos(2*pi*25*t) + cos(2*pi*35*t+pi/10);

subplot(3,2,1);
plot(t,x)
%axis([min(t) max(t) -3 3])
axis tight
xlabel('Time, s')
ylabel('x(n)')
title('Two sinusoids with a rectangular window')

Xf=fft(x);
resolution=fs/length(Xf);
f=(0:length(Xf)-1)*resolution;
Xf_power = Xf.*conj(Xf);  % power spectral density
index=1:length(Xf)/4;
subplot(3,2,2);
stem(f(index),Xf_power(index))
xlabel('Frequency, Hz')
ylabel('Power spectral density')
title('Spectrum')
axis([min(f(index)) max(f(index)) 0 max(Xf_power)*1.1])
axis tight

% Using Hanning window
x=x-mean(x);%減掉直流成分
x=x.*hanning(length(x))';
subplot(3,2,3);
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
subplot(3,2,4);
stem(f(index),Xf_power(index))
xlabel('Frequency, Hz')
ylabel('Power spectral density')
title('Spectrum')
axis([min(f(index)) max(f(index)) 0 max(Xf_power)*1.1])

% Zero padding
x_ZP=zeros(1,length(x)*5);
x_ZP(1:length(x))=x;
t_ZP=(0:length(x_ZP)-1)*1/fs;
subplot(3,2,5);
plot(t_ZP,x_ZP)
axis([min(t_ZP) max(t_ZP) -3 3])
title('x(n)')
ylabel('x(n)')
xlabel('Time, s')
title('Zero padding')

Xf=fft(x,length(x)*5);
resolution=fs/length(Xf);
f=(0:length(Xf)-1)*resolution;
Xf_power = Xf.*conj(Xf);  % power spectral density
index=1:length(Xf)/4;
subplot(3,2,6);
stem(f(index),Xf_power(index))
xlabel('Frequency, Hz')
ylabel('Power spectral density')
title('Zero-padding spectrum')
axis([min(f(index)) max(f(index)) 0 max(Xf_power)*1.1])
