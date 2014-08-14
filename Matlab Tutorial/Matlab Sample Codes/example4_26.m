clear
t = (0:1/1000:2);
x1 = sin(2*pi*50*t);
y = modulate(x1,200,1000,'am');
x2 = demod(y,200,1000,'am');

subplot(311)
plot(t(1:150),x1(1:150)); 
title('Original Signal');

subplot(312) 
plot(t(1:150),y(1:150)); 
title('Modulated Signal');

subplot(313)
plot(t(1:150),x2(1:150)); 
title('Demodulated Signal');
