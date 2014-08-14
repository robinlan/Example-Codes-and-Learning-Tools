clear
[b,a] = butter(9,400/1000);
freqz(b,a,256,2000)