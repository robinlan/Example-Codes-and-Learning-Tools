clear
subplot(2,1,1)
% Generate a periodic Gaussian pulse signal at 10 kHz, with 50% bandwidth.
% The pulse repetition frequency is 1 kHz, sample rate is 50 kHz, and pulse train length is 10 msec.
% The repetition amplitude should attenuate by 0.8 each time.  
% Uses a function handle to refer to the generator function.

t1 = 0:1/50E3:10E-3;
d1 = [0:1/1E3:10E-3;0.8.^(0:10)]';
y1 = pulstran(t1,d1,'gauspuls',10E3,0.5);
plot(t1,y1,'linewidth',1.2)
xlabel('Time')
ylabel('Amplitude')
title('Gaussian-modulated sinusoidal pulse train')
grid on

subplot(2,1,2)
% Generate an asymmetric sawtooth waveform with a repetition frequency of 3 Hz and a sawtooth width of 0.1 sec.
% The signal is to be 1 second long with a sample rate of 1 kHz.

t2 = 0 : 1/1E3 : 1;                       % 1 kHz sample freq for 1 sec
d2 = 0 : 1/3 : 1;                         % 3 Hz repetition freq
y2 = pulstran(t2,d2,'tripuls',0.1,-1); 
plot(t2,y2,'linewidth',2)
xlabel('Time')
ylabel('Amplitude')
title('Asymmetric saw-toothed pulse train')
grid on
