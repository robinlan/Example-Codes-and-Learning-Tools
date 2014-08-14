clear
[b,a] = butter(9,400/1000);
w = linspace(0,pi);
freqz(b,a,w)