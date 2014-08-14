string = 'I bet there is a bat on the boat';
pattern = 'b(\w*)t';
[start, finish, token] = regexp(string, pattern);
fprintf('There are %d matched substrings:\n', length(start));
for i=1:length(start)
	fprintf('\t%d: matched="%s", token="%s"\n', i, ...
		string(start(i):finish(i)), string(token{i}(1):token{i}(2)));
end