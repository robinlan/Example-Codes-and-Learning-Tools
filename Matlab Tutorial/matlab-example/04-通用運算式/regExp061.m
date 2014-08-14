string = 'I bet there is a bat on the boat for bait';
pattern = 'b(\w*?)t(.*?)b(\w*?)t';
[start, finish, token] = regexp(string, pattern);
fprintf('There are %d matched substrings:\n', length(start));
for i=1:length(start)
	fprintf('\t%d: Matched substring="%s"\n', i, string(start(i):finish(i)));
	fprintf('\t\t%d matched substrings in the parentheses:\n', size(token{i},1));
	for j=1:size(token{i},1)
		fprintf('\t\t\t"%s"\n', string(token{i}(j,1):token{i}(j,2)));
	end
end