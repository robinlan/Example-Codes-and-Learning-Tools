dept1 = char('ee', 'cs', 'econ');	% dept1 是一個字元陣列
cellData = cellstr(dept1);		% 將 dept1 轉成一個異值陣列 cellData
dept2 = char(cellData);			% 將 cellData 轉換成字元陣列 dept2
isequal(dept1, dept2)			% 測試 dept1 和 dept2 是否相等