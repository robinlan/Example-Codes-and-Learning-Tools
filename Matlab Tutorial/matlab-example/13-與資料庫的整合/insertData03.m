copyfile('score01.mdb', 'score02.mdb');		% 將 score01.mdb 拷貝到 score02.mdb
dsn = 'dsnScore02';				% 設定資料來源名稱（指到 score02.mdb）
logintimeout(5);				% 設定嘗試連結資料庫的時間
conn = database(dsn, '', '');			% 連結資料庫
exec(conn, 'DELETE * from score');		% 先刪除所有的資料
colNames={'studentId', 'studentName', 'overall'};	% 新增資料的欄位名稱
dataValues={'0001', '陳江村', 100; '0002', '林政源', 97};		% 新增資料對應的欄位值
insert(conn, 'score', colNames, dataValues);	% 加入兩筆資料
cursor = exec(conn, 'select * from score');	
cursor = fetch(cursor);
newScore = cursor.data				% 顯示更新後 final 欄位的資料
close(cursor);					% 結束 cursor 物件
close(conn);					% 結束資料庫連結