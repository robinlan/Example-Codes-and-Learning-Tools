str = 'I walk up, he walks up, we are all walking up.';
pat = 'walk(\w*) up';
newStr = regexprep(str, pat, 'sleep$1 tight');
fprintf('%s\n', newStr);