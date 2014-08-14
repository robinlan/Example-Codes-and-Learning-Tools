string = 'I like Chapter 12, particularly Section 4!';
pattern = '(Chapter|Section) [1-9]\d?';
[start, finish] = regexp(string, pattern);
fprintf('Matched substrings:\n');
for i=1:length(start)
	fprintf('\t%d: %s\n', i, string(start(i):finish(i)));
end