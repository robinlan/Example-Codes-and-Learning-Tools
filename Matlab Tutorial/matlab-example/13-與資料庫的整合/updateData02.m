copyfile('score01.mdb', 'score02.mdb');	% 將 score01.mdb 拷貝到 score02.mdb
dsn = 'dsnScore02';			% 設定資料來源名稱（指到 score02.mdb）
logintimeout(5);			% 設定嘗試連結資料庫的時間
conn = database(dsn, '', '');		% 連結資料庫
colNames={'final'};			% 需更新的欄位
dataValues={100};			% 更新後的欄位值
update(conn, 'score', colNames, dataValues, 'where final>=80');
% 設定進行資料查詢的 SQL 命令
sql = 'select studentName, final from score';	% 設定 SQL 命令
cursor = exec(conn, sql);	
cursor = fetch(cursor);
newScore = cursor.data				% 顯示更新後 final 欄位的資料
close(cursor);					% 結束 cursor 物件
close(conn);					% 結束資料庫連結