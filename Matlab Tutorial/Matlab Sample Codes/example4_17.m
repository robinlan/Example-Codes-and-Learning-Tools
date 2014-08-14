clear
t = 0:.00025:1;                              % 時間向量
x = sin(2*pi*30*t) + sin(2*pi*60*t);
y = decimate(x,4);
subplot(121)
stem(x(1:120))                               % 原始訊號
axis([0 120 -2 2])          
title('原始訊號')
subplot(122)
stem(y(1:30))                                % 降頻過的訊號
title('降頻過的訊號')
