clear all		% 清除所有變數
x = [1 4 -2 3 -1 -5];
for i = 1:length(x),
	if x(i)>0,
		fprintf('x(%g) = %g is positive\n', i, x(i));
	else
		fprintf('x(%g) = %g is negative or zero\n', i, x(i));
	end  
end
