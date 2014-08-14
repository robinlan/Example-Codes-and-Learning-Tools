clear
[b,a] = butter(4,0.4)                        % Design Butterworth lowpass
[h,w] = freqz(b,a,64);                       % Compute frequency response
wt = ones(size(w));                          % Create unity weighting vector
[b30,a30] = invfreqz(h,w,3,3,wt,30)          % 30 iteration

[b3,a3] = invfreqz(h,w,3,3)                  % Model: n = 3, m = 3
fvtool(b,a,b3,a3,b30,a30)

sum(abs(h-freqz(b3,a3,w)).^2)                % Total error, algorithm 1, third-order IIR
sum(abs(h-freqz(b30,a30,w)).^2)              % Total error, algorithm 2, 30 iteration

