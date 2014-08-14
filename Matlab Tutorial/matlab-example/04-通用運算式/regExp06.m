string = 'My email is roger_jang@hotmail.com, jang@cs.nthu.edu.tw, and jang@wayne.cs.nthu.edu.tw.';
pattern = '\w+@\w+\.\w{2,3}|\w+@\w+\.\w+\.\w+';
[start, finish] = regexp(string, pattern);
fprintf('Matched substrings:\n');
for i=1:length(start)
	fprintf('\t%d: %s\n', i, string(start(i):finish(i)));
end