theta = linspace(0, 2*pi, 50);
rho = sin(0.5*theta);
[x, y] = pol2cart(theta, rho);	% 由極座標轉換至直角座標
compass(x, y);			% 畫出以原點為向量起始點的羅盤圖
