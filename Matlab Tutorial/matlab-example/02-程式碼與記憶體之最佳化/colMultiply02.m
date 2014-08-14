fprintf('MATLAB version = %s\n', version);
n = 10000;
x = rand(3, n);
y1 = zeros(size(x));
a = 1:n;
% === 第 1 種方法：for-loop operation
tic
for i = 1:n
	y1(:,i)=x(:,i)*a(i);
end
time1 = toc;
% === 第 2 種方法：vectorized operation which requires extra space
tic
y2 = x*diag(a);
time2 = toc;
% === 第 3 種方法：builtin command for vectorized operation
tic
y3 = bsxfun(@times, x, a);
time3 = toc;

fprintf('time1 = %g sec, time2 = %g sec, time3 = %g sec\n', time1, time2, time3);
fprintf('time1/time2 = %g\n', time1/time2);
fprintf('time1/time3 = %g\n', time1/time3);