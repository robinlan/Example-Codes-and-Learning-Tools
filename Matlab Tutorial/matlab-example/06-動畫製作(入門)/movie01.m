clear M					% 清除電影資料矩陣 M
n = 50;					% 抓取 50 個畫面
figure('Renderer','zbuffer');		% Only used in MS Windows
peaks;
fprintf('抓取畫面中...\n');
for i = 1:n
	view([-37.5+i*360/n, 30]);	% 改變觀測角度
	M(i) = getframe;		% 抓取畫面，並存入電影資料矩陣 M  
end
fprintf('播放電影中...\n');
movie(M, 3);				% 播放電影三次