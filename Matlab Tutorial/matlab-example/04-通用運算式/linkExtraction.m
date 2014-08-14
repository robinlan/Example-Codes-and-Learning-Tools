fileName = 'regExp.htm';
string=fileread(fileName);
pattern = '<a href="(.*?)">(.*?)</a>';
[start, finish, token] = regexp(string, pattern);
fprintf('由檔案 "%s" 抽取出 %d 個連結：:\n', fileName, length(start));
for i=1:length(start)
	fprintf('\t%d: 連結文字："%s", 連結網址："%s"\n', ...
	i, string(token{i}(2,1):token{i}(2,2)), string(token{i}(1,1):token{i}(1,2)));
end