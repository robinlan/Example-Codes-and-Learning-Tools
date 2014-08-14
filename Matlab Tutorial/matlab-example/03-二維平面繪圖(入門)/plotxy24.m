theta = linspace(0, 4*pi, 30);  
rho = 10;  
[x, y] = pol2cart(theta, rho);	% 由極座標轉換至直角座標  
feather(x, y);	% 繪製羽毛圖
axis image