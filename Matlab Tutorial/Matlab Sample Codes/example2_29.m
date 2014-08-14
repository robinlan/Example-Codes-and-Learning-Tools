clear
n = 61;
wo = 0.3;
dp = 0.02;
ds = 0.008;
b = fircls1(n,wo,dp,ds,'both');    % Both textual display and plot magnitude response 

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
