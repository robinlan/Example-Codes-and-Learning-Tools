mat = magic(100);
t0 = clock;
for i = 1:10; mesh(mat); drawnow; end
elapsedTime = etime(clock, t0)	% 顯示實際經過時間
t0 = cputime;
for i = 1:10; mesh(mat); drawnow; end
cpuTime = cputime-t0		% 顯示 CPU 佔用時間