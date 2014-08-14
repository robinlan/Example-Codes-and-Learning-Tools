str = 'a--b---b----d';
pat1 = 'a(.*)b(.*)d';	% 「越左越貪」的比對
[start, finish, token] = regexp(str, pat1);
fprintf('「越左越貪」的比對結果：\n');
for j=1:size(token{1},1)
	fprintf('\ttoken%d = "%s"\n', j, str(token{1}(j,1):token{1}(j,2)));
end
pat2 = 'a(.*?)b(.*)d';	% 利用問號來修正
[start, finish, token] = regexp(str, pat2);
fprintf('利用問號來進行「最小比對」的結果：\n');
for j=1:size(token{1},1)
	fprintf('\ttoken%d = "%s"\n', j, str(token{1}(j,1):token{1}(j,2)));
end