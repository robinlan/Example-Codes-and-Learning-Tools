t = 0:0.4:4*pi;
y = cos(t).*exp(-t/5);
stairs(t, y);
hold on				% 保留舊圖形
stem(t, y);			% 疊上針頭圖
hold off