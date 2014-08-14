fid = fopen('unicode.txt');	
line = fgetl(fid)		% 讀取一列檔案內容並印出  
fclose(fid);
leng=length(line)		% 顯示字串變數長度