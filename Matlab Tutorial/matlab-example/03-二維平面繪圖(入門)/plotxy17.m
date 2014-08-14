x = linspace(0,2*pi,30);	 % 在 0 到 2π 間，等分取 30 個點  
y = sin(x);  
e = y*0.2;  
errorbar(x,y,e)				  % 圖形上加上誤差範圍 e   
