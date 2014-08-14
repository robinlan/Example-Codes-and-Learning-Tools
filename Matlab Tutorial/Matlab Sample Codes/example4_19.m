clear
t = 0:0.01:1.27;                              % 時間向量
s1 = sin(2*pi*45*t);                          % 原始訊號
s2 = s1 + 0.5*[zeros(1,20) s1(1:108)];
xhat = cceps(s2);
plot(t,xhat)
title('complex cepstrum')
