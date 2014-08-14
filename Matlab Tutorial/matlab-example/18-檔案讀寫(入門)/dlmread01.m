fprintf('data.dlm 的內容：\n');
type data.dlm			% 列出 data.dlm 的內容
A = dlmread('data.dlm', '\t')	% 將 data.dlm 的內容讀到矩陣 A