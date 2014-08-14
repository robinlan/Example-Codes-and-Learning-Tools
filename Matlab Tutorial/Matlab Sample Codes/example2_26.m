clear
b = remez(21,[0.05 1],[1 1],'h');         % Highpass Hilbert
bb = remez(20,[0.05 0.95],[1 1],'h');     % Bandpass Hilbert
fvtool(b,1,bb,1)

fs = 1000;                                % Sampling frequency
t = (0:1/fs:2)';                          % Two second time vector
x = sin(2*pi*300*t);                      % 300 Hz sine wave example signal
xh = filter(bb,1,x);                      % Hilbert transform of x

xd = [zeros(10,1); x(1:length(x)-10)];    % Delay 10 samples
xa = xd + j*xh;                           % Analytic signal
