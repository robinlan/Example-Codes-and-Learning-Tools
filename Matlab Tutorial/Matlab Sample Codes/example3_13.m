clear
load mtlb
fs = 1000;
[P1,f] = pwelch(mtlb,hamming(256),128,1024,fs);
[P2,f] = pyulear(mtlb,14,1024,fs);
plot(f,10*log10(P1),':',f,10*log10(P2)); grid
ylabel('PSD Estimates (dB/Hz)');
xlabel('Frequency (Hz)');
legend('Welch','Yule-Walker AR')