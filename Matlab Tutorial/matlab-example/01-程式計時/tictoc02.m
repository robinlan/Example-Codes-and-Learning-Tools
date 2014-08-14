timer1=tic;				% 馬表一開始計時
n=100*(1:10);
for i=1:length(n)
	timer2=tic;			% 馬表二開始計時
	z=inv(rand(n(i)));		% inv 指令是用來計算反矩陣
	time(i)=toc(timer2);		% 馬表二停止計時
end
fprintf('Overall time = %f sec\n', toc(timer1));	% 馬表一停止計時
plot(n, time, '.-');
xlabel('Matrix dimensions');
ylabel('Elapsed time (in sec)');