load seamount.mat
plot(x, y, '.');
k = convhull(x, y);
hold on, plot(x(k), y(k), 'r'), hold off	% 畫出最小凸多邊