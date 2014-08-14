departments = char('ee', 'cs', 'econ');
dept1 = departments(1,:);	% (1,:)代表第一列的元素  
dept2 = deblank(dept1);		% 使用 deblank 指令來移除尾部的空白字元   	
len1 = length(dept1)		% 顯示變數 dept1 的長度   
len2 = length(dept2)		% 顯示變數 dept2 的長度