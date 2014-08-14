fplot(@humps, [-1, 2]); grid on
z1 = fzero(@humps, 1.5);
z2 = fzero(@humps, [-1, 1]);
line(z1, humps(z1), 'marker', 'o', 'color', 'r');	% 畫出第一個零點的位置
line(z2, humps(z2), 'marker', 'o', 'color', 'r');	% 畫出第二個零點的位置