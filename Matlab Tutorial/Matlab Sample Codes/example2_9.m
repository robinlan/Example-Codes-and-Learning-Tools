clear
n = 50;
Wn = 0.4;
b = fir1(n,Wn);
freqz(b,1,512);
