clear
b = firls(30,[0 0.9],[0 0.9],'differentiator');
[H,f] = freqz(b,1,31,2);
plot(f,abs(H))
grid on
xlabel('Normalized Frequency')
ylabel('Magnitude')
