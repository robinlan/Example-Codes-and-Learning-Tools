[x, y, z, v] = flow(10);
slice(x, y, z, v, [6 9.5], 2.5, [-2 0]);
colorbar;	% 顯示顏色與函數值的對照表
size(x)