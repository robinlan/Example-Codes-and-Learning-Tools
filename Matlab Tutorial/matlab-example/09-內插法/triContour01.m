load seamount.mat
xi = linspace(min(x), max(x), 50);
yi = linspace(min(y), max(y), 50);
[xi, yi] = meshgrid(xi, yi);  
zi = griddata (x, y, z, xi, yi, 'cubic');  
[c, h] = contourf(xi, yi, zi, 'b-');
clabel (c, h);
colorbar;	% 顯示顏色與函數值的對照表