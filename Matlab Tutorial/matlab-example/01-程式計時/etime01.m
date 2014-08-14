t0 = clock;			% 記錄現在的時間
a = inv(rand(1000));		% 執行反矩陣運算
elapsedTime = etime(clock, t0)	% 計算所耗費的總時間