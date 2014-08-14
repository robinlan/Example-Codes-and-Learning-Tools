string = 'Draft      beer, 			not   people.';
pattern = '\s+';
string2 = regexprep(string, pattern, ' ');	% 將多個空白壓縮成一個
fprintf('原字串：%s\n', string);
fprintf('修改後：%s\n', string2);