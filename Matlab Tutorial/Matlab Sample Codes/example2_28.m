clear
n = 129;
f = [0 0.3 0.5 0.7 0.9 1];
amp = [0 0.5 0 1 0];
up = [0.005 0.51 0.03 1.02 0.05];
lo = [-0.005 0.49 -0.03 0.98 -0.05];
b = fircls(n,f,amp,up,lo,'both');  % Both textual display and plot magnitude response
fvtool(b,1)
