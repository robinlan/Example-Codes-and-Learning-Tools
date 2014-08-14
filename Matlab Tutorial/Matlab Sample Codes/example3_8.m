clear
randn('state',1)
fs = 1000;                         % Sampling frequency
t = (0:0.3*fs)./fs;                % 301 samples
xn = 2*sin(2*pi*150*t)+8*sin(2*pi*140*t) + 5*randn(size(t));
figure(1)
subplot(211)
periodogram(xn,rectwin(length(xn)),1024,fs);
axis([0 500 -25 10])
subplot(212)
pwelch(xn,rectwin(150),75,512,fs);
axis([0 500 -25 10])

figure(2)
pwelch(xn,hamming(100),75,512,fs);
axis([0 500 -25 10])