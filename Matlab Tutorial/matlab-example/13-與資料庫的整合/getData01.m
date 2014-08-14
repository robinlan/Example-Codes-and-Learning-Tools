dsn = 'dsnSong01';				% 設定資料來源名稱（指到 song01.mdb）
logintimeout(5);				% 設定嘗試連結資料庫的時間
conn = database(dsn, '', '');			% 連結資料庫
sql = 'select * from song';			% 執行設定 SQL 命令
cursor = exec(conn, sql);			% 執行 SQL 命令，並傳回 cursor 物件	
cursor = fetch(cursor, 8);			% 經由 cursor 物件，抓取 8 筆資料
songData = cursor.data				% 將資料傳至 MATLAB 變數 songData
close(cursor);					% 結束 cursor 物件
close(conn);					% 結束資料庫連結