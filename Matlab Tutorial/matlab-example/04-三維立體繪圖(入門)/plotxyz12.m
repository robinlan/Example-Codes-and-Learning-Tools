[X, Y, Z] = peaks;
Z(10:20,10:20) = nan;	% 將 Z 矩陣的一部分代換為 nan
surf(X, Y, Z);
axis tight