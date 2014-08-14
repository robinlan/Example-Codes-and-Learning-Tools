clear
[b,a] = butter(5,0.4);                   % Lowpass Butterworth
[b,a] = cheby1(4,1,[0.4 0.7]);           % Bandpass Chebyshev Type I
[b,a] = cheby2(6,60,0.8,'high');         % Highpass Chebyshev Type II
[b,a] = ellip(3,1,60,[0.4 0.7],'stop');  % Bandstop elliptic
