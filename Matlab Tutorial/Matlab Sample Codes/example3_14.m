clear

load mtlb
fs = 1000;
[P1,f] = pburg(mtlb(1:512),14,1024,fs); % 14th order model
[P2,f] = pyulear(mtlb(1:512),14,1024,fs); % 14th order model
plot(f,10*log10(P1),':',f,10*log10(P2)); grid
ylabel('Magnitude (dB)'); xlabel('Frequency (Hz)');
legend('Burg','Yule-Walker AR')