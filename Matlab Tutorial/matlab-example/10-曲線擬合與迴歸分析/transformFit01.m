load data2.txt
x = data2(:, 1);		% 已知資料點的 x 座標
y = data2(:, 2);		% 已知資料點的 y 座標
A = [ones(size(x)) x];
theta = A\log(y);
subplot(2,1,1)
plot(x, log(y), 'o', x, A*theta); xlabel('x'); ylabel('ln(y)');
title('ln(y) vs. x');
legend('Actual value', 'Predicted value');
a = exp(theta(1))			% 辨識得到之參數
b = theta(2)				% 辨識得到之參數
y2 = a*exp(b*x);
subplot(2,1,2);
plot(x, y, 'o', x, y2); xlabel('x'); ylabel('y');
legend('Actual value', 'Predicted value');
title('y vs. x');
fprintf('誤差平方和 = %d\n', sum((y-y2).^2));