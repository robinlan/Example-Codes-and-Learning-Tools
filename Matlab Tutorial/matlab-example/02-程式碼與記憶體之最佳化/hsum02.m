fprintf('MATLAB version = %s\n', version);
ns = 1000*(1:1000);
for i=1:length(ns)
	n=ns(i);
	% 第一種方法：for-loop operation
	tic
	total1 = 0;
	for j = 1:n
		total1 = total1+1/j;
	end
	time1 = toc;
	% 第二種方法：vectorized operation
	tic
	total2 = sum(1./(1:n));
	time2 = toc;
	% 計算倍數
	speedupFactor(i)=time1/time2;
end
plot(ns, speedupFactor, '.-'); grid on
xlabel('n'); ylabel('time1/time2');