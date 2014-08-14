fid = fopen('big5.txt');	
line = fgetl(fid);		% 讀取一列檔案內容
fclose(fid);
double(line)			% 顯示字串內碼