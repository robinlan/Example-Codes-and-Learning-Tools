tempFile = [tempname, '.html'];				% 指定暫存檔案
urlwrite('http://www.google.com.tw', tempFile);		% 將網頁內容寫到檔案
dos(['start ', tempFile]);				% 開啟此檔案