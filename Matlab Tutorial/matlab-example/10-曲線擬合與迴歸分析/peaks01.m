pointNum = 10;
[xx, yy, zz] = peaks(pointNum);
zz = zz + randn(size(zz));	% ¥[¤JÂø°T
surf(xx, yy, zz);
axis tight