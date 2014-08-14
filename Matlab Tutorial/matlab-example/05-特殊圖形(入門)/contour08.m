h = polar([0 0], [0 1]);		% 產生在極座標上的一條直線
delete(h);				% 移除上述圖形，但留下極座標圖軸
hold on
contour(xx, yy, ff, 50);		% 畫出等高線
hold off