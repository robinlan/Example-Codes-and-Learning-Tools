A = [1 2; 3 4];
n = 100000;
clear detValue;				% 清除變數 detValue
tic
for i = 1:n
	detValue(i) = det(A^i);
end
time1=toc;
detValue = zeros(1, n);		% 預先配置所須矩陣
tic
for i = 1:n
	detValue(i) = det(A^i);
end
time2=toc;
fprintf('time1 = %g, time2 = %g, time1/time2 = %g\n', time1, time2, time1/time2);