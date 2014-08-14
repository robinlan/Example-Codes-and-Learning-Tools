clear

load mtlb
fs = 1000;
[P1,f] = pcov(mtlb(1:64),14,1024,fs);          % 14th order model
[P2,f] = pmcov(mtlb(1:64),14,1024,fs);         % 14th order model
plot(f,10*log10(P1),':',f,10*log10(P2)); grid
xlabel('Frequency (Hz)');
ylabel('Magnitude (dB)'); 
legend('Covariance','Modified Covariance')
