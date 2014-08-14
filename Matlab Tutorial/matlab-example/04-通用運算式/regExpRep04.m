str = 'are you ready';
pat = '^([^ ]+) *([^ ]+)';
rep = '$2 $1';
str2 = regexprep(str, pat, rep);
fprintf('­ì¦r¦ê¡G%s\n', str);
fprintf('­×§ï«á¡G%s\n', str2);