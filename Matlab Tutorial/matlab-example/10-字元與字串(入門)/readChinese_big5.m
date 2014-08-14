encoding='Big5';
fid = fopen('chinese_big5.txt', 'r', 'n', encoding);	
line = fgetl(fid);		% 讀取一列檔案內容並印出  
fclose(fid);
disp(line)

filewrite({line}, 'test.txt');
