clear
t = 0:0.001:1;                              % 時間向量
x = sin(2*pi*30*t) + sin(2*pi*60*t);
y = interp(x,4);
subplot(121)
stem(x(1:30))                               % 原始訊號
axis([0 30 -2 2])          
title('原始訊號')
subplot(122)
stem(y(1:120))                                % 升頻過的訊號
axis([0 120 -2 2])
title('升頻過的訊號')
