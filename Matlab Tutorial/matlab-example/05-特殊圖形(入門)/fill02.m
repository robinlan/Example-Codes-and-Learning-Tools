t = 0:0.4:4*pi;
y = sin(t).*exp(-t/5);
fill(t, y, 'y');			% 'y' 為黃色
hold on				% 保留舊圖形
stem(t, y, 'b');		% 疊上藍色針頭圖
hold off