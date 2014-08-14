clear all;
ns = 0:2000;
for j=1:length(ns)
	t = 0:ns(j);
	% 第一種方法：for-loop operation
	y=zeros(1,length(t));
	tic
	for i = 1:length(t)
		y(i) = sin(t(i)/100);
	end
	time1=toc;
	% 第二種方法：vectorized operation
	tic
	y = sin(t)/100;
	time2=toc;
	% 計算倍數
	ratio(j)=time1/time2;
end
plot(ns, ratio, '.-');
