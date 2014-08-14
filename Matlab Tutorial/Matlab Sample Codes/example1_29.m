clear
t = (0:1/100:10-1/100);              % Time vector
x = sin(2*pi*15*t) + sin(2*pi*40*t); % Signal
y = fft(x);                          % Compute DFT of x
m = abs(y);                          % Magnitude
p = unwrap(angle(y));                % Phase
f = (0:length(y)-1)*99/length(y);    % Frequency vector
plot(f,m); 
title('Magnitude');
set(gca,'XTick',[15 40 60 85]);
figure; 
plot(f,p*180/pi); 
title('Phase');
set(gca,'XTick',[15 40 60 85]);