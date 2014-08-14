dsn = 'dsnScore01';				% 設定資料來源名稱（指到 score01.mdb）
logintimeout(5);				% 設定嘗試連結資料庫的時間
conn = database(dsn, '', '');			% 連結資料庫
sql = 'select * from score';			% 執行設定 SQL 命令
cursor = exec(conn, sql);			% 執行 SQL 命令，並傳回 cursor 物件
setdbprefs('DataReturnFormat', 'structure');	% 設定 cursor 傳回資料格式是結構陣列
cursor = fetch(cursor, 10);			% 經由 cursor 物件，抓取 10 筆資料
score = cursor.Data				% 經資料設定至 score 變數
close(cursor);					% 結束 cursor 物件
close(conn);					% 結束資料庫連結
setdbprefs('DataReturnFormat', 'cellarray');	% 改回預設的資料格式