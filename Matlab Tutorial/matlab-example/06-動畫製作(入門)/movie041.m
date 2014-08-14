x = 0:0.1:8*pi;
plot(x, cos(x), 'color', 'r', 'lineWidth', 10);
h = line(x, sin(x).*exp(-x/5), 'EraseMode', 'xor', 'color', 'b', 'lineWidth', 10);
axis([-inf inf -1 1]);			% 設定圖軸的範圍
grid on					% 畫出格線
for i = 1:5000
	y = sin(x-i/50).*exp(-x/5);
	set(h, 'ydata', y);		% 設定新的 y 座標
	drawnow				% 立即作圖
end