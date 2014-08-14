x = 6*rand(100, 1)-3;	% [-3, 3] 之間的 100 個均勻分佈亂數  
y = 6*rand(100, 1)-3;	% [-3, 3] 之間的 100 個均勻分佈亂數  
z = peaks(x, y);  
[xi, yi] = meshgrid(-3:0.2:3, -3:0.2:3);  
zi = griddata(x, y, z, xi, yi);
mesh(xi, yi, zi);	% 畫出曲面
hold on; plot3(x, y, z, 'o'); hold off	% 畫出資料點
axis tight; hidden off;
figure; mesh(xi, yi, zi); view(2); axis image	% 曲面俯視圖
hold on; plot3(x, y, z+1, 'o'); hold off		% 畫出資料點
k = convhull(x, y);
line(x(k), y(k), 10*ones(1,length(k)), 'color', 'r');	% Add convex hull