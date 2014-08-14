string = 'I bet there is a bat on board';
pattern = 'b[aeiou]t';
[start, finish] = regexp(string, pattern);
fprintf('Matched substrings:\n');
for i=1:length(start)			% 列印出比對結果
	fprintf('\t%d: %s\n', i, string(start(i):finish(i)));
end