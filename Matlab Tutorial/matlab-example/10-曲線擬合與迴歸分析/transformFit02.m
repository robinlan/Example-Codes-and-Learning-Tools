load data2.txt
x = data2(:, 1);		% 已知資料點的 x 座標
y = data2(:, 2);		% 已知資料點的 y 座標
A = [ones(size(x)) x];
theta = A\log(y);
a = exp(theta(1))		% 辨識得到之參數
b = theta(2)			% 辨識得到之參數
theta0 = [a, b];		% fminsearch 的啟始參數
theta = fminsearch(@errorMeasure3, theta0, [], data2);
x = data2(:, 1);
y = data2(:, 2);
y2 = theta(1)*exp(theta(2)*x);
plot(x, y, 'o', x, y2); xlabel('x'); ylabel('y');
legend('Actual value', 'Predicted value');
title('y vs. x');
fprintf('誤差平方和 = %d\n', sum((y-y2).^2));