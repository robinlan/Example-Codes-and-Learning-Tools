clear
b = firls(255,[0 0.25 0.3 1],[1 1 0 0]) ;
[H,f] = freqz(b,1,256,2) ;
plot(f,abs(H))
grid on
xlabel('Normalized Frequency')
ylabel('Magnitude')
