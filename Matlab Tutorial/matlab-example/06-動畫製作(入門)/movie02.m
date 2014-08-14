clear M				% 清除電影資料矩陣 M
r=linspace(0, 4, 30);		% 圓盤的半徑
t=linspace(0, 2*pi, 50);	% 圓盤的極座標角度
[rr, tt]=meshgrid(r, t);
xx=rr.*cos(tt);			% 產生圓盤上的 x 座標
yy=rr.*sin(tt);			% 產生圓盤上的 y 座標
zz=peaks(xx,yy);		% 產生 peaks 在極座標的資料
n = 30;				% 抓取 30 個畫面
scale = cos(linspace(0, 2*pi, n));
figure('Renderer','zbuffer');	% Only used in MS Windows
fprintf('抓取畫面中...\n');
for i = 1:n
	surf(xx, yy, zz*scale(i));		% 畫圖
	axis([-inf inf -inf inf -8.5 8.5]);	% 固定圖軸的範圍
	box on
	M(i) = getframe;			% 抓取畫面，並存入電影資料矩陣 M  
end
fprintf('播放電影中...\n');
movie(M, 5);					% 播放電影 5 次