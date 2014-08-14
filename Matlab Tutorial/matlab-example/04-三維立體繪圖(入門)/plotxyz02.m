x = linspace(-2, 2, 25);		% 在 x 軸 [-2,2] 之間取 25 點  
y = linspace(-2, 2, 25);		% 在 y 軸 [-2,2] 之間取 25 點  
[xx,yy] = meshgrid(x, y);		% xx 和 yy 都是 25×25 的矩陣  
zz = xx.*exp(-xx.^2-yy.^2);		% zz 也是 25×2 的矩陣  
surf(xx, yy, zz);				% 畫出立體曲面圖  
colormap('default')				% 顏色改回預設值 