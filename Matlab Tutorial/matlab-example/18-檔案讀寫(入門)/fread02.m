fid = fopen('test2.txt', 'r');
myData = fread(fid, 4)		% 只讀 4 個位元組
fclose(fid);