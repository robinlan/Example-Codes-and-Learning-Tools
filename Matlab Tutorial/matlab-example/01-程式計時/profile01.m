profile on			% 啟動計時器
for i = 1:1000
	a = inv(rand(100));	% 計算 100x100 亂數矩陣的反矩陣
	b = mean(rand(100));	% 計算 100x100 亂數矩陣的每一直行平均值
end
profile viewer			% 呈現計時結果
