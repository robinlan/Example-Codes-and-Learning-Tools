clear
randn('state',0)
fs = 1000;                                    % Sampling frequency
t = (0:fs)/fs;                                % One second worth of samples
xn = sin(2*pi*50*t) + randn(1,length(t));

h = ones(1,10)/10;                            % Moving average filter
yn = filter(h,1,xn);
[HEST,f] = tfe(xn,yn,256,fs,256,128,'none');
H = freqz(h,1,f,fs);

subplot(2,1,1); 
plot(f,abs(H));
xlabel('Frequency (Hz)');
ylabel('Magnitude');
title('Actual Transfer Function Magnitude');

subplot(2,1,2); 
plot(f,abs(HEST));
title('Transfer Function Magnitude Estimate');
xlabel('Frequency (Hz)');
ylabel('Magnitude');
axis([0 500 0 1])