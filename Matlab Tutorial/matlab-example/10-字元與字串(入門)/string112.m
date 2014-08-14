chinese = '今日事，今日畢';
out1 = class(chinese)	% out1 的值是回傳值 "char"，代表變數 chinese 是字串變數
x = chinese+1;
out2 = ischar(x)	% out2 的值是 0，代表變數 x 不是一個字串變數