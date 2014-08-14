fid = fopen('test2.txt', 'r');
myData = fread(fid);
char(myData')		% 驗證所讀入的資料是否正確
fclose(fid);