clear
[n,Wn,beta,ftype] = kaiserord([1500 2000],[1 0],...
                              [0.01 0.1],8000);
b = fir1(n,Wn,ftype,kaiser(n+1,beta),'noscale');
c = kaiserord([1500 2000],[1 0],[0.01 0.1],8000,'cell');
b = fir1(c{:});

[H,f] = freqz(b,1,1024,8000);
plot(f,abs(H))
grid on
xlabel('Frequency (Hz)')
ylabel('Magnitude Squared')
