clear
fs = 8000;
f = [1000 1300 2210 2410];
a = [0 1 0];
dev = [0.01 0.05 0.01];
[n,Wn,beta,ftype] = kaiserord(f,a,dev,fs);
n = n + rem(n,2);
b = fir1(n,Wn,ftype,kaiser(n+1,beta),'noscale');
[H,f] = freqz(b,1,1024,fs);
plot(f,abs(H))
grid on
xlabel('Frequency (Hz)')
ylabel('Magnitude Squared')
