x = linspace(0, 2*pi);	% 在 0 到 2π 間，等分取 100 個點  
y1 = sin(x);  
y2 = exp(-x);  
plotyy(x, y1, x, y2);	% 畫出兩個刻度不同的 y 軸，分別是 y1, y2  
