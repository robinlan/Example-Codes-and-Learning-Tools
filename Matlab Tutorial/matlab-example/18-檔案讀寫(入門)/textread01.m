fprintf('data.txt 的內容：\n');
type data.txt			% 列出 data.txt 的內容
[name, hobby, age] = textread('data.txt', '%s%s%d')