clear
F = [0.0 0.3  0.4 0.6  0.7 0.9]; 
A = [0.0 1.0  0.0 0.0  0.5 0.5];
b = firls(24,F,A,'hilbert');
for i=1:2:6
    plot([F(i) F(i+1)],[A(i) A(i+1)],'r--')
    hold on
end
[H,f] = freqz(b,1,512,2);
plot(f,abs(H))
legend('Ideal','firls Design')
xlabel('Normalized Frequency')
ylabel('Magnitude')
grid on
hold off
legend('Ideal','Ideal','Ideal','firls Design')
