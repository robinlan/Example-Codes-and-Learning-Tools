x = 6*rand(100,1)-3;		% x 為介於 [-3, 3] 的 100 點亂數  
y = 6*rand(100,1)-3;		% y 為介於 [-3, 3] 的 100 點亂數  
z = peaks(x, y);			% z 為 peaks 指令產生的 100 點輸出  
[X, Y] = meshgrid(-3:0.1:3);  
Z = griddata(x, y, z, X, Y, 'cubic');	% 用 cubic 內差法進行內差  
meshc(X, Y, Z);
hold on
plot3(x, y, z, '.', 'MarkerSize', 16);	% 晝出 100 個取樣  
hold off
axis tight