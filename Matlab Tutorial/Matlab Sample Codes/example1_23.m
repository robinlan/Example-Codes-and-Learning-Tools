clear
h = fir1(25,0.4);
[H,f] = freqz(h,1,512,2);
figure(1)
hold on
plot(f,angle(H)*180/pi); 
grid on
xlabel('Normalized frequency')
ylabel('Phase (degrees)')
title('Gaussian-modulated sinusoidal pulse')
hold off

figure(2)
hold on
plot(f,unwrap(angle(H))*180/pi);
grid on
xlabel('Normalized frequency')
ylabel('Phase (degrees)')
title('Gaussian-modulated sinusoidal pulse')
hold off
