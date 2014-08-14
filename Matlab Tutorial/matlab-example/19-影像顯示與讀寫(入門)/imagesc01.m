temp=colormap(gray);
newMap=temp(1:6:end, :);
X = peaks;
colormap(newMap);
imagesc(X);
colorbar;
min(min(X))		% 顯示 X 的最小值
max(max(X))		% 顯示 X 的最大值
