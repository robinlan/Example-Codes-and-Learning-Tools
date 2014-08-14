[x,y] = meshgrid(-3:1:3); 	% 產生 -3 至 3 的格子點
z = peaks(x,y);				% 產生 peaks 曲面資料
[xi, yi] = meshgrid(-3:.25:3); 	% 生產內插點
zi1 = interp2(x, y, z, xi, yi, 'nearest');
zi2 = interp2(x, y, z, xi, yi, 'linear');
zi3 = interp2(x, y, z, xi, yi, 'cubic');
zi4 = interp2(x, y, z, xi, yi, 'spline');
subplot(2,3,1); surf(x, y, z); axis tight; title('Original');
subplot(2,3,3); surf(xi, yi, zi1); axis tight; title('Nearest');
subplot(2,3,4); surf(xi, yi, zi2); axis tight; title('Linear');
subplot(2,3,5); surf(xi, yi, zi3); axis tight; title('Cubic');
subplot(2,3,6); surf(xi, yi, zi4); axis tight; title('Spline');
% 以下畫出等高線
figure
subplot(2,3,1); contour(x, y, z, 20); title('Original');
subplot(2,3,3); contour(xi, yi, zi1, 20); title('Nearest');
subplot(2,3,4); contour(xi, yi, zi2, 20); title('Linear');
subplot(2,3,5); contour(xi, yi, zi3, 20); title('Cubic');
subplot(2,3,6); contour(xi, yi, zi4, 20); title('Spline');