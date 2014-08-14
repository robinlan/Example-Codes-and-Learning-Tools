string = 'bt bat bet ban bit boat beet berp boaet baeiout';
pattern = 'b[aeiou]{2,3}t';
[start, finish] = regexp(string, pattern);
fprintf('Matched substrings:\n');
for i=1:length(start)
	fprintf('\t%d: %s\n', i, string(start(i):finish(i)));
end