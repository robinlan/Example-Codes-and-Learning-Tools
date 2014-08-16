% This program and data is partially modified or extracted from WWW of R.
% Rangayyan, 2002 and written by Hsiao-Lung chan Ph.D, 2011

clear all               % clears all active variables
close all

emg = load('emg_dog2.dat');
fs = 10000;         % sampling rate for emg
emg = emg*1000/20;   % gain = 20,000; emg now in micro volts

slen = length(emg);
t=(1:slen)/fs; 

clf
set(gcf,'color','w')  % set white background
subplot(4,1,1)
plot(t, emg)
axis([min(t) max(t) min(emg)*1.1 max(emg)*1.1])
ylabel('Amplitude, uV');
title('EMG')


% Envelope of EMG obtained by root-mean-square
dur=round(0.05*fs);   % 50-ms duration
fs1 = 1000/dur;     % sampling rate for envelope
index=1:dur; %dur自己設
segNo=floor(slen/dur);%floor直接把小數去掉
env1=zeros(1,segNo);%不寫也沒關係,但效率會較好 一開始就給空間
for s=1:segNo
  env1(s)=std(emg(index));%std均方根
  index=index+dur;
end
slen1 = length(env1);
t1=[1:slen1]/fs1;   

subplot(4,1,2)
plot(t1, env1);
title('EMG envelope by RMS');
axis([min(t1) max(t1) 0 max(env1)*1.1])

% Envelope of EMG obtained by Hilbert transform
emgAnalytic=hilbert(emg);
emgAbs=abs(emgAnalytic);
dur=round(0.05*fs);   % 50-ms duration
fs2 = 1000/dur;     % sampling rate for envelope
index=1:dur;
segNo=floor(slen/dur);
env2=zeros(1,segNo);
for s=1:segNo
  env2(s)=mean(emgAbs(index));
  index=index+dur;
end
slen2 = length(env2);
t2=(1:slen2)/fs2;   

subplot(4,1,3)
plot(t2, env2);
title('EMG envelope by Hilbert transform-based method');
xlabel('Time, s');
axis([min(t2) max(t2) 0 max(env2)*1.1])


% Envelope of EMG obtained by a full-wave rectifier and a lowpass filter
hs=fs/2;   % half sampling rate of EMG
wn=8/hs;    % cutoff frequency = 5 Hz
[b, a]=butter(4, wn);  % Get 4-th order IIR coefficients
emgRectify=abs(emg);
env3=filtfilt(b,a,emgRectify);
slen3 = length(env3);
t3=(1:slen3)/fs;  

subplot(4,1,4)
plot(t3, env3);
title('EMG envelope by full-wave rectifier & lowpass filter');
xlabel('Time, s');
axis([min(t3) max(t3) 0 max(env3)*1.1])


