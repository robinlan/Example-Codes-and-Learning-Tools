string = 'Some terms: RU486, Y2K, 900GHz, B2B, B2C';
pattern = '\D\d\D';
[start, finish] = regexp(string, pattern);
fprintf('Matched substrings:\n');
for i=1:length(start)
	fprintf('\t%d: %s\n', i, string(start(i):finish(i)));
end