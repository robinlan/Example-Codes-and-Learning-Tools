string = 'Two cards: 1234-5678-9012-3456 and 0987-6543-2109-8765';
pattern = '(\d{4}-){3}\d{4}';
[start, finish] = regexp(string, pattern);
fprintf('Matched substrings:\n');
for i=1:length(start)
	fprintf('\t%d: %s\n', i, string(start(i):finish(i)));
end