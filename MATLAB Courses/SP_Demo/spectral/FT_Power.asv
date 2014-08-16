clear
fs=500;
t=0:1/fs:3;
x = cos(2*pi*25*t);

subplot(3,2,1);
plot(t,x)
title(['Variance = ' num2str(std(x)^2)])
axis([min(t) max(t) -3 3])
xlabel('Time, s')
ylabel('x(n)')

Xf=fft(x);
resolution=fs/length(Xf);
f=(0:length(Xf)-1)*resolution;
Xf_power = Xf.*conj(Xf);  % power spectral density
Xf_power=Xf_power/fs/length(Xf);
totpower=sum(Xf_power*resolution);
index=1:length(Xf)/4;
subplot(3,2,3);
plot(f(index),Xf_power(index))
xlabel('Frequency, Hz')
ylabel('PSD, V^2/Hz')
title(['Total power = ' num2str(totpower)])
axis([min(f(index)) max(f(index)) 0 max(Xf_power)*1.1])


% Using Hanning window
x=x.*hanning(length(x))';
subplot(3,2,2);
plot(t,x)
axis([min(t) max(t) -3 3])
title(['Variance = ' num2str(std(x)^2)])
ylabel('x(n)')
xlabel('Time, s')

Xf=fft(x);
resolution=fs/length(Xf);
f=(0:length(Xf)-1)*resolution;
Xf_power = Xf.*conj(Xf);  % power spectral density
Xf_power=Xf_power/fs/sum(Hanning(length(Xf)).^2);
totpower=sum(Xf_power*resolution);
index=1:length(Xf)/4;
subplot(3,2,4);
plot(f(index),Xf_power(index))
xlabel('Frequency, Hz')
ylabel('PSD, V^2/Hz')
title(['Total power = ' num2str(totpower)])
axis([min(f(index)) max(f(index)) 0 max(Xf_power)*1.1])

NFFT=1024;
Xf=pwelch(x,rectwin(NFFT),0,NFFT);
resolution=fs/length(Xf);
f=(0:length(Xf)-1)*resolution;
Xf_power = Xf.*conj(Xf);  % power spectral density
Xf_power=Xf_power/pi/NFFT;
totpower=sum(Xf_power*resolution)*2;
index=1:length(Xf)/2;
subplot(3,2,5);
plot(f(index),Xf_power(index))
xlabel('Frequency, Hz')
ylabel('PSD, V^2/Hz')
title(['Welch total power = ' num2str(totpower)])
axis([min(f(index)) max(f(index)) 0 max(Xf_power)*1.1])

Xf=pwelch(x,hanning(NFFT),0,NFFT);
resolution=fs/length(Xf);
f=(0:length(Xf)-1)*resolution;
Xf_power = Xf.*conj(Xf);  % power spectral density
Xf_power=Xf_power/pi/NFFT;
totpower=sum(Xf_power*resolution)*2;
index=1:length(Xf)/2;
subplot(3,2,6);
plot(f(index),Xf_power(index))
xlabel('Frequency, Hz')
ylabel('PSD, V^2/Hz')
title(['Welch total power (Hanning) = ' num2str(totpower)])
axis([min(f(index)) max(f(index)) 0 max(Xf_power)*1.1])

