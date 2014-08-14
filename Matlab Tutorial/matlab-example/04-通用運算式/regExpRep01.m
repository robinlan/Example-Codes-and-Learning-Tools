str = 'I bet there is a bat in the boat!';
pat = 'b[aeiou]+t';
newStr = regexprep(str, pat, 'xxx');
fprintf('%s\n', newStr);