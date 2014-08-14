clear
fs = 1000;                                   % Sampling frequency
b = remez(21,[0 1],[0 pi*fs],'d');
bb = remez(20,[0 0.9],[0 0.9*pi*fs],'d');
fvtool(b,1,bb,1)
