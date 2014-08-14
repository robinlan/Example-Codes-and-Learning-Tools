x = fzero(@humps, 1.5);		% 求靠近 1.5 附近的根
y = humps(x);			% 帶入求值
fprintf('humps(%f) = %f\n', x , y);