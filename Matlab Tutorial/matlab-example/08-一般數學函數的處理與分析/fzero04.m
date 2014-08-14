opt = optimset('disp', 'iter');		% 顯示每個 iteration 的結果    
a = fzero(@humps, [-1, 1], opt)