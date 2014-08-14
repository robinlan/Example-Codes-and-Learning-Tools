% 眔材UI北ン计
pointNum = round(str2num(get(h2, 'string')));
% 璝计び┪び玥砞﹚10
if pointNum <= 1 | pointNum > 100,
	pointNum = 10;
	set(h2, 'string', int2str(pointNum));
end

% 沮┮眔计礶peaksΡ
[xx, yy, zz] = peaks(pointNum);
surf(xx, yy, zz);
axis tight;

% 沮材UI北ン∕﹚琌璶礶絬
if get(h1, 'value')==1,
	grid on;
else
	grid off;
end
