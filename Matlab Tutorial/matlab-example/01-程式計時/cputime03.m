t1=clock;
t0=cputime;			% 記錄現在的時間
a=inv(rand(1000));		% 執行反矩陣運算
mesh(a);
myCpuTime = cputime-t0		% 計算 CPU 所耗費的時間
myElapsedTime=etime(clock, t1)