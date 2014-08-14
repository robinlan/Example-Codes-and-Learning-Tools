copyfile('score01.mdb', 'score02.mdb');		% 將 score01.mdb 拷貝到 score02.mdb
dsn = 'dsnScore02';				% 設定資料來源名稱（指到 score02.mdb）
logintimeout(5);				% 設定嘗試連結資料庫的時間
conn = database(dsn, '', '');			% 連結資料庫
% 抓出所有的資料
cursor = exec(conn, 'select * from score');	% 執行 SQL 命令，並傳回 cursor 物件
cursor = fetch(cursor);				% 經由 cursor 物件，抓取全部資料
score = cursor.data;				% 將資料傳至 MATLAB 變數 score
temp = columnnames(cursor);			% 資料庫欄位名稱
eval(['fieldNames = {', temp, '}'';']);		% 將欄位名稱指定到 fieldNames 變數
score = cell2struct(score, fieldNames, 2);	% 將異質陣列 score 轉成結構陣列
% 對每一筆資料進行運算，並將結果存回資料庫
for i=1:length(score)
	homework=(score(i).homework1+score(i).homework2+score(i).homework3)/3;
	overallScore=homework*0.3+score(i).midterm*0.3+score(i).final*0.4;
	% 將資料寫入資料庫
	sql = ['UPDATE score SET overall=', num2str(overallScore), ' where studentID=''', score(i).studentID, ''''];
	exec(conn, sql);
end
% 列出 score 資料表
cursor = exec(conn, 'select * from score');	
cursor = fetch(cursor);
newScore = cursor.data;				% 更新後的資料
temp = columnnames(cursor);			% 資料庫欄位名稱
eval(['fieldNames = {', temp, '}'';']);		% 將欄位名稱指定到 fieldNames 變數
newScore = cell2struct(newScore, fieldNames, 2);% 將異質陣列 newScore 轉成結構陣列
close(cursor);					% 結束 cursor 物件
close(conn);					% 結束資料庫連結
struct2html(newScore);				% 顯示結構陣列 newScore 於瀏覽器