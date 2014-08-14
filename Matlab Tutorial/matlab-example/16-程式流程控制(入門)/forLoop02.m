h = zeros(6);	% 變數 x 是一個 6×6 大小的零矩陣
for i = 1:6
	for j = 1:6
		h(i,j) = 1/(i+j-1);
	end
end
format rat		% 使用分數形式來顯式所有數值
h			% 顯示 h