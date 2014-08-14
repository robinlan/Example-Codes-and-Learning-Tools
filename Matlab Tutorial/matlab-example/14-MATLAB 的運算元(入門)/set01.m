x = [1 2 3 4 5 6];
y = [1 1 5 5 9 9 9];
union_result = union(x, y)		% 聯集
intersect_result = intersect(x, y)	% 交集
setdiff_result = setdiff(x, y)		% 差集
setxor_result = setxor(x, y)		% XOR 運算
setuniq_result = unique(y)		% 取相異元素
ismember_result = ismember(9, y)	% 元素 9 是否屬於集合 y