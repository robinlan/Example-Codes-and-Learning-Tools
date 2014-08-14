clear
f = [0 0.6 0.6 1]; 
m = [1 1 0 0];
b = fir2(30,f,m);
[h,w] = freqz(b,1,128);
plot(f,m,'-',w/pi,abs(h),'.')
legend('Ideal','fir2 Designed')
title('Comparison of Frequency Response Magnitudes')
