function [ave1, ave2] = func7(vector1, vector2)

if nargin == 1,		% 只有一個輸入變數
	ave1 = sum(vector1)/length(vector1);
end

if nargout == 2,	% 有兩個輸出變數
	ave1 = sum(vector1)/length(vector1);
	ave2 = sum(vector2)/length(vector2);
end

fprintf('mfilename = %s\n', mfilename);
fprintf('inputname = %s\n', inputname);