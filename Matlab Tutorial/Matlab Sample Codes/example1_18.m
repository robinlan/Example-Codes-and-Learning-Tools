clear
b = 1;
a = [1 -0.9];
[h,t]=impz(b,a);
stem(t,h)
xlabel('Time')
ylabel('Amplitude')
title('Exponential Decay H(n) = 0.9n of the Single Pole System')
