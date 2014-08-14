dsn = 'dsnSong01';				% 設定資料來源名稱（指到 song01.mdb）
logintimeout(5);				% 設定嘗試連結資料庫的時間
conn = database(dsn, '', '');			% 連結資料庫
sql = 'select * from song';			% 設定 SQL 命令
cursor = exec(conn, sql);			% 執行 SQL 命令，並傳回 cursor 物件
cursor = fetch(cursor, 10);			% 經由 cursor 物件，抓取 10 筆資料
fprintf('資料筆數 = %d\n', rows(cursor));	% 顯示資料筆數
fprintf('欄位個數 = %d\n', cols(cursor));	% 顯示欄位個數
fprintf('欄位名稱 = %s\n', columnnames(cursor));% 顯示資料庫欄位名稱
fprintf('欄位寬度 = %d\n', width(cursor, 3));	% 顯示第三個欄位的寬度
attributes = attr(cursor, 3)			% 顯示第三個欄位的所有資訊
close(cursor);					% 結束 cursor 物件
close(conn);					% 結束資料庫連結