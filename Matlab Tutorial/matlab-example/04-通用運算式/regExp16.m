string = '1234-5678-9012-3456 and A123456789 and 5715131';
pattern = '\d{4}-\d{4}-\d{4}-\d{4}|[A-Z]\d{9}|\d{7}';
[start, finish] = regexp(string, pattern);
fprintf('Matched substrings:\n');
for i=1:length(start)
	fprintf('\t%d: %s\n', i, string(start(i):finish(i)));
end