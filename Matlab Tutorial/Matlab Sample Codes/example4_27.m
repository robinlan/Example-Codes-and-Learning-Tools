clear
fs = 10000;
t = 0:1/fs:2;
x = sawtooth(2*pi*t,.75);
y = vco(x,[0.1 0.4]*fs,fs);
specgram(y,512,fs,kaiser(256,5),220)
% [B,F,T] = SPECGRAM(A,NFFT,Fs,WINDOW,NOVERLAP)
specgramdemo(y,fs)
