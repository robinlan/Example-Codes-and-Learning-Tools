fprintf('MATLAB version = %s\n', version);
matlabpool local 4

n = 100;
rowMedian1=zeros(1000, 1);
rowMedian2=zeros(1000, 1);
% === 第一種方法：common for-loop
tic
for i = 1:1000
	rowMedian1(i) = max(eig(rand(n)));
end
time1 = toc;
% === 第二種方法：parallel for-loop using 2 workers
tic
parfor (i = 1:1000, 2)	% 只用兩個核心
	rowMedian2(i) = max(eig(rand(n)));
end
time2 = toc;
fprintf('time1 = %g, time2 = %g, speedup factor = %g\n', time1, time2, time1/time2);

matlabpool close