copyfile('score01.mdb', 'score02.mdb');	% 將 score01.mdb 拷貝到 score02.mdb
dsn = 'dsnScore02';			% 設定資料來源名稱（指到 score02.mdb）
logintimeout(5);			% 設定嘗試連結資料庫的時間
conn = database(dsn, '', '');		% 連結資料庫
% 產生新資料表 friend
sql = 'CREATE TABLE friend (fullName char(6), birthday date)';
exec(conn, sql);
% 插入第一筆資料
sql = 'INSERT INTO friend (fullName, birthday) VALUES (''王瓊雯'', ''1983/11/03'')';
exec(conn, sql);
% 插入第二筆資料
sql = 'INSERT INTO friend (fullName, birthday) VALUES (''葉佳慧'', ''1982/09/22'')';
exec(conn, sql);
% 列出所有資料
cursor = exec(conn, 'select * from friend');	
cursor = fetch(cursor);
friend = cursor.data				% 顯示更新後 friend 資料表的資料
close(cursor);					% 結束 cursor 物件
close(conn);					% 結束資料庫連結