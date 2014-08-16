% This demo program was developed Hsiao-Lung Chan, Ph.D. October 30, 2009
% Six-point average

clf
load ecg2_250.mat;
x=ecg;
fs=250;
t=(0:length(x)-1)/fs;

%x = sin(2*pi*50*t) + 0.5*randn(size(t));
subplot(3,3,1)
plot(t,x)
ylabel('x(n)')
xlabel('Time, s')
title('ecg')
axis([min(t) max(t) min(x)*1.1 max(x)*1.1])

Xf=fft(x);
resolution=fs/length(Xf);
f=(0:length(Xf)-1)*resolution;
Xf_magnitude = abs(Xf); 
subplot(3,3,2)
index=1:length(Xf_magnitude)/2;
plot(f(index),Xf_magnitude(index))
xlabel('Frequency, Hz')
ylabel('Magnitude')
title('Spectra')
axis([min(f(index)) max(f(index)) 0 max(Xf_magnitude(index))*1.1])

NFFT=1024;
b=[1 0 0 0 0 0 -2 0 0 0 0 0 1];
a=[1 -2 1];
z=filter(b,a,x);
[h,f] = freqz(b,a,NFFT);
h_magnitude=abs(h);
h_phase=angle(h);
subplot(3,3,4)
plot(f,h_magnitude);
title('lowpass')

subplot(3,3,3)
plot(t,z)



b=zeros(1,33);
b(1)=-1;
b(17)=32;
b(18)=-32;
b(33)=1;
a=[32 -32];                         
NFFT=1024;
[h,f] = freqz(b,a,NFFT);
f=f/pi*fs/2;
h_magnitude=abs(h);
h_phase=angle(h);
subplot(3,3,5)
plot(f,h_magnitude);
axis([0 max(f) 0 max(h_magnitude)*1.1])
xlabel('Frequency, Hz')
ylabel('Magnitude')
title('Frequency response of filter')



% subplot(3,2,4)
% plot(f,h_phase);
% axis([min(f) max(f) min(h_phase)*1.1 max(h_phase)*1.1])
% xlabel('Frequency, Hz')
% ylabel('Phase')
% title('Frequency response of filter')


y=filter(b,a,z);
subplot(3,3,6)
plot(t,y)


for n=2:999
    y1(n)=y(n)-y(n-1);
end
t=(1:length(y)-1)/fs;
subplot(3,3,7)
plot(t,y1)
ylabel('y(n)')
xlabel('Time, s')
title('Filtered signal')
axis([min(t) max(t) min(y) max(y)])

Yf=fft(y1);
resolution=fs/length(Yf);
f=(0:length(Yf)-1)*resolution;
Yf_magnitude = abs(Yf);
subplot(3,3,8)
index=1:length(Yf_magnitude)/2;
plot(f(index),Yf_magnitude(index))
xlabel('Frequency, Hz')
ylabel('Magnitude')
title('Spectra')
axis([min(f(index)) max(f(index)) 0 max(Yf_magnitude(index))*1.1])

y2=y1.^2;%平方

% %積分不能用轉移函數
% b=[0.5 0.5];
% a=[1 -1];
% y3=filter(b,a,y2);
% subplot(3,3,9)
% plot(t,y3)

%積分使用六點平均
b=ones(1,32)*1/32;
a=1;
y3=filter(b,a,y2);
subplot(3,3,9)
plot(t,y3)
axis([0 2 0 2])

