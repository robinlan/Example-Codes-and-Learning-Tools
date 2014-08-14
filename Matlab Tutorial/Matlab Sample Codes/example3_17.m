clear

load mtlb
fs = 1000;
[P1,f] = pmusic(mtlb(1:64),4);        % 4th order model
[P2,f] = peig(mtlb(1:64),4);          % 4th order model
plot(f,10*log10(P1),':',f,10*log10(P2)); grid
xlabel('Frequency (Hz)');
ylabel('Magnitude (dB)'); 
legend('MUSIC','Eigenvector')
