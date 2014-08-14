clear
fs = 8000;
f = [1000 1500];
a = [1 0];
dev = [0.05 0.01];
[n,Wn,beta,ftype] = kaiserord(f,a,dev,fs);
b = fir1(n,Wn,ftype,kaiser(n+1,beta),'noscale');
freqz(b)

