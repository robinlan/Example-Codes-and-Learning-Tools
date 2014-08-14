string = 'My credit number is "1234-5678-9012-3456".';
pattern = '\d{4}-\d{4}-\d{4}-\d{4}';
[start, finish] = regexp(string, pattern);
fprintf('Matched substrings:\n');
for i=1:length(start)
	fprintf('\t%d: %s\n', i, string(start(i):finish(i)));
end