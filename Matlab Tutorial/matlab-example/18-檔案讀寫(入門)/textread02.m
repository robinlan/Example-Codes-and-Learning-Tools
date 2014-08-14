fprintf('data2.txt 的內容：\n');
type data2.txt			% 列出 data2.txt 的內容
[name, hobby, age] = textread('data2.txt', '%s%s%d', 'delimiter', '\t')