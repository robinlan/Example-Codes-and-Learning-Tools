fprintf('MATLAB version = %s\n', version);
n = 100000000;
% === 第一種方法：for-loop operation
tic
total1 = 0;
for i = 1:n
	total1 = total1+1/i;
end
time1 = toc;
% === 第二種方法：vectorized operation
tic
total2 = sum(1./(1:n));
time2 = toc;
fprintf('time1 = %g, time2 = %g, speedup factor = %g\n', time1, time2, time1/time2);