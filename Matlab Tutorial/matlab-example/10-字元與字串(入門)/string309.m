fid = fopen('big5.txt');
line = fgetl(fid);			% 讀取一列檔案內容
fclose(fid);
line2 = native2unicode(line, 'big5')	% 使用 native2unicode 將被拆開的中文字結合在一起
leng = length(line2)			% 顯示字串長度