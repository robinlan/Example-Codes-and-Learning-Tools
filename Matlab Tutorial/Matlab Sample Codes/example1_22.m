clear
[b,a] = butter(6,300/500);
[h,w] = freqz(b,a,512,1000);
m = abs(h); 
p = angle(h);
subplot(121);
semilogy(w,m,'linewidth',2); 
title('Magnitude');

subplot(122);
plot(w,p*180/pi,'linewidth',2); 
title('Phase');