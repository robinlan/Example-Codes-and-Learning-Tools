clear
n = 100;
w = boxcar(n);
[W,f] = freqz(w/sum(w),1,512,2);
plot(f,20*log10(abs(W)));
xlabel('Normalized Frequency')
ylabel('Magnitude dB')
title('N = 100')