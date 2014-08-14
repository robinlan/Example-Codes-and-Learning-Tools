string = 'I like Chapter 2, Chapter 10, and Chapter 25 of this book!';
pattern = 'Chapter [1-9][0-9]?';
[start, finish] = regexp(string, pattern);
fprintf('Matched substrings:\n');
for i=1:length(start)
	fprintf('\t%d: %s\n', i, string(start(i):finish(i)));
end