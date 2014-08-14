x1 = rand(10000, 1);
x2 = randn(10000, 1);
subplot(2,1,1); hist(x1, 40); title('均勻分佈');
subplot(2,1,2); hist(x2, 40); title('高斯分佈');
set(findobj(gcf, 'type', 'patch'), 'EdgeColor', 'w');	% 改邊線為白色
