clear
n = 20;                     % Filter order
f = [0 0.1 0.15 0.25 0.3 0.4 0.45 0.55 0.6 0.7 0.75 0.85 0.9 1];
a = [1 1 0 0 1 1 0 0 1 1 0 0 1 1];
b_I = firls(n,f,a);
b_II = remez(n,f,a);

fvtool(b_I,1,b_II,1)
% legend('Filter (firls)','Filter (remez)')
