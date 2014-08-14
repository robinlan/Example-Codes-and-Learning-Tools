x = 0:0.1:4*pi;
subplot(2, 2, 1); plot(x, sin(x));		% 此為左上角圖形
subplot(2, 2, 2); plot(x, cos(x));		% 此為右上角圖形    	
subplot(2, 2, 3); plot(x, sin(x).*exp(-x/5));	% 為左下角圖形
subplot(2, 2, 4); plot(x, x.^2);		% 此為右下角圖形 