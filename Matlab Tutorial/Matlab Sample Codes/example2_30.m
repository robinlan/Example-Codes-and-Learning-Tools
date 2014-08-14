clear
n = 55;
wo = 0.3;
dp = 0.02;
ds = 0.004;
wp = 0.28;
ws = 0.32;
k = 10;
b = fircls1(n,wo,dp,ds,wp,ws,k,'both');  % Both textual display and plot magnitude response

subplot(311)
ylabel('Full Band')
grid

subplot(312)
ylabel('Passband Ripple')
grid

subplot(313)
ylabel('Stopband Ripple')
grid
xlabel('Normalized Frequency  (\times\pi rad/sample)')
fvtool(b,1)
