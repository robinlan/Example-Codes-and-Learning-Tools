string = 'The food is under the bar in the barn.';
pattern1 = 'foo.*bar';
[start, finish] = regexp(string, pattern1);
fprintf('\tGreedy match: %s\n', string(start:finish));
pattern2 = 'foo.*?bar';
[start, finish] = regexp(string, pattern2);
fprintf('\tMinimal match: %s\n', string(start:finish));