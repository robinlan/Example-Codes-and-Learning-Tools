clear M				% 清除電影資料矩陣 M
load clown.mat
image(X); colormap(map);	% 畫出小丑臉
n = 30;				% 抓取 30 個畫面
scale = cos(linspace(0, 2*pi, n));
fprintf('抓取畫面中...\n');
for i = 1:n
	colormap(((i-1)*(1-map)+(n-i)*map)/(n-1));	% 改變色盤矩陣
	M(i) = getframe;			% 抓取畫面，並存入電影資料矩陣 M  
end
fprintf('播放電影中...\n');
movie(M, -5);					% 播放電影 5 次