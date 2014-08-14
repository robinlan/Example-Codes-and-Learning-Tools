str1 = 'Chapter 5 is my favorite';
str2 = 'I like Chapter 2';
pat = '^Chapter';
fprintf('regexp(''%s'', ''%s'') = %d\n', str1, pat, regexp(str1, pat));
fprintf('regexp(''%s'', ''%s'') = %d\n', str2, pat, regexp(str2, pat));