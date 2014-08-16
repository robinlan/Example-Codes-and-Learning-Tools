% This program and data is modified or extracted from WWW of R. Rangayyan, 2002
% The 'safety' speech signal (male speaker)

close all
clear all
soundx = wavread('safety.wav');    % converts the sound from wave format
fs = 8000;
len = length(soundx);
t = (1:length(soundx))/fs;
plot(t, soundx)
axis([min(t) max(t) min(soundx)*1.1 max(soundx)*1.1])
xlabel('Time, sec');
ylabel('Amplitude');
title('Sound waveform SAFETY');
sound(soundx) % to play the wav file as sound             
