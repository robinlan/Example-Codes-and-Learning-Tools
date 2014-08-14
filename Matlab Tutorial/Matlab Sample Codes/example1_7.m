clear
subplot(2,1,1)
% Plot a 50 kHz Gaussian RF pulse with 60% bandwidth and sampled at a rate of 1 MHz.
% Truncate the pulse where the envelope falls 40 dB below the peak.
tc = gauspuls('cutoff',50E3,.6,[],-40);
t  = -tc : 1E-6 : tc;    
yi = gauspuls(t,50E3,0.6);
plot(t,yi,'linewidth',2)
axis([-4E-5 4E-5 -2 2])
xlabel('Time')
ylabel('Amplitude')
title('Gaussian-modulated sinusoidal pulse')
grid on

subplot(2,1,2)
t = 0:1/1000:2;                                 % 2 secs @ 1kHz sample rate
y = chirp(t,0,1,150);                           % Start @ DC, cross 150Hz at t=1sec
specgram(y,256,1000,256,250)                    % Display the spectrogram
title('spectrogram from a linear chirp signal')