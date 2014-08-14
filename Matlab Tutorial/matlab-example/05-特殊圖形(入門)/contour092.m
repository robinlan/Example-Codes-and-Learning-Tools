t = linspace(0, 2*pi, 61);		% 角度的格子點
r = 0:0.05:1;				% 長度的格子點
[tt, rr] = meshgrid(t, r);		% 產生二維的格子點
zz = rr.*exp(sqrt(-1)*tt);		% 複數表示
ff = abs(zz.^3-1);			% 曲面的函數值
h = polar([0 2*pi], [0 1]);		% 產生在極座標上的一條直線
delete(h);				% 移除上述圖形，但留下極座標圖軸
hold on
contour(xx, yy, ff, 20);		% 等高線
surf(xx, yy, ff);			% 曲面圖
hold off
view(-19, 22);				% 設定觀測角度