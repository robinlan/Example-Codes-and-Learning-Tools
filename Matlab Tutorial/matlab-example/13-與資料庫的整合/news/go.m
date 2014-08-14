close all; clear all

logintimeout(5);
DSN = 'news';

Conn = database(DSN, '', '');
%ping(Conn)
sql = 'select * from news order by news_date';
cursorA = exec(Conn, sql);
cursorA = fetch(cursorA);
news = cursorA.data;
temp = columnnames(cursorA);
eval(['fieldNames = {', temp, '}'';']); 
news = cell2struct(news, fieldNames, 2);
studentNum = length(news);
close(cursorA);
close(Conn);