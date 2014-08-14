clear
n = 20;                     % Filter order
f = [0 0.4 0.5 1];          % Frequency band edges
a = [1 1 0 0];              % Desired amplitudes
b_I = firls(n,f,a);
b_II = remez(n,f,a);

fvtool(b_I,1,b_II,1)