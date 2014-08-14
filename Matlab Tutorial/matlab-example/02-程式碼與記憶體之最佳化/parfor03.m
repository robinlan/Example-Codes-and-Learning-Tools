fprintf('MATLAB version = %s\n', version);
matlabpool local 4

n = 10000;
x = rand(n);
rowMedian1=zeros(n, 1);
rowMedian2=zeros(n, 1);
% === 第一種方法：for-loop operation
tic
for i = 1:n
	rowMedian1(i) = median(x(i,:));
end
time1 = toc;
% === 第二種方法：vectorized operation
tic
parfor i = 1:n
	rowMedian2(i) = median(x(i,:));
end
time2 = toc;
fprintf('time1 = %g, time2 = %g, speedup factor = %g\n', time1, time2, time1/time2);

matlabpool close