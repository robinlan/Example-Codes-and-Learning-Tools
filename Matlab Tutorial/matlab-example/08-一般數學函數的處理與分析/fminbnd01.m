[x, minValue] = fminbnd(@humps, 0.3, 1)			% 使用 fminbnd 指令找出最小值的發生點
fplot(@humps, [0.3, 1]); grid on
line(x, minValue, 'marker', 'o', 'color', 'r');		% Plot the minimum point