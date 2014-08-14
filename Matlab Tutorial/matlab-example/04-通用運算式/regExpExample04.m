str = 'Try complex string manupulation using regexp';
pat = '\w*m\w*';

str = 'bat cat can car coat court cut ct caoueouat';
pat = 'c[aeiou]+t';

[s, f] = regexp(str, pat);
fprintf('string = %s\n', str);
fprintf('pattern = %s\n', pat);
fprintf('Matched substrings:\n');
for i=1:length(s)
	fprintf('\t%d: %s\n', i, str(s(i):f(i)));
end

str = {'Madrid, Spain' 'Romeo and Juliet' 'MATLAB is great'};
pat = {'[A-Z]', '\s', 'g.*t'};
start=regexp(str, pat)

str = 'six sides of a hexagon sixxxxxs';
pat = 's(\w*)s';
[start, finish, token] = regexp(str, pat);
fprintf('There are %d matched strings:\n', length(start));
for i=1:length(start)
	fprintf('\t%d: matched="%s", token="%s"\n', i, str(s(i):f(i)), str(token{i}(1):token{i}(2)));
end