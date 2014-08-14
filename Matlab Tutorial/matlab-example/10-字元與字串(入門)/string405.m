A = [1 2 1; 3 5 6 ];  
B = mat2str(A)		% 將矩陣 A 轉成字串 B   
A2 = eval(B);		% 再將字串 B 轉回矩陣 A2
isequal(A, A2)		% 測試 A 和 A2 是否相等