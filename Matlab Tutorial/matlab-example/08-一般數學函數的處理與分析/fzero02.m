x = fzero(@humps, [-1, 1]);	% 求落於區間 [-1, 1] 的根
y = humps(x);			% 帶入求值
fprintf('humps(%f) = %f\n', x , y);