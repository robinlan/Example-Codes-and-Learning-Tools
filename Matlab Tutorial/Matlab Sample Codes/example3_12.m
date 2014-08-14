clear
randn('state',0)
fs = 1000;                                    % Sampling frequency
t = (0:fs)/fs;                                % One second worth of samples
xn = sin(2*pi*50*t) + randn(1,length(t));

h = ones(1,10)/10;                            % Moving average filter
yn = filter(h,1,xn);

Cxy = cohere(xn,yn,256,fs,256,128,'none')
cohere(xn,yn,256,fs,256,128,'none')