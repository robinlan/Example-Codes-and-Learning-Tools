load clown.mat		% 載入小丑影像資料，含變數 X 和 map
fprintf('min(min(X)) = %d\n', min(min(X)));
fprintf('max(max(X)) = %d\n', max(max(X)));
fprintf('size(map, 1) = %d\n', size(map, 1));