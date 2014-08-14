clear all;				% 清除所有的變數
A = [1 0 0 0 0; 0 0 0 3 0; 0 4 0 0 0];	% 完全矩陣
S = sparse(A);				% 將完全矩陣 A 轉換成稀疏矩陣 S
whos