DSN = 'dsnScore01';				% 設定資料來源名稱（指到 score01.mdb）
logintimeout(5);				% 設定嘗試連結資料庫的時間
conn = database(DSN, '', '');			% 連結資料庫
sql = 'select * from score order by studentID';	% 設定 SQL 命令
cursor = exec(conn, sql);			% 執行 SQL 命令，並傳回 cursor 物件
cursor = fetch(cursor);				% 經由 cursor 物件，抓取全部資料
score = cursor.data;				% 將資料傳至 MATLAB 變數 score
temp = columnnames(cursor);			% 顯示資料庫欄位名稱
eval(['fieldNames = {', temp, '}'';']);		% 將欄位名稱指定到 fieldNames 變數
score2 = cell2struct(score, fieldNames, 2);	% 將異質陣列 score 轉成結構陣列 score2
close(cursor);					% 結束 cursor 物件
close(conn);					% 結束資料庫連結
struct2html(score2);			% 顯示結構陣列 score2 於瀏覽器