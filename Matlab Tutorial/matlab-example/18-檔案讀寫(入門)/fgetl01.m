fid = fopen('mean.m', 'r');
while feof(fid)==0		% feof 測試檔案指標是否已到達結束位置
	line = fgetl(fid);
	disp(line);
end