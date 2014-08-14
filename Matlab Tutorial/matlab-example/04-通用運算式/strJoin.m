function str = strJoin(tokenList, delimiter)
% STRJOIN Join a token list with the given delimiter to generate a string
%	Usage:
%	str = strJoin(tokenList, delimiter)

%	Roger Jang, 20030331

if nargin==0; selfdemo; return; end
if length(tokenList)==0; str=''; return; end
if length(tokenList)==1; str=tokenList{1}; return; end

str=tokenList{1};
for i=2:length(tokenList)
	str=[str, delimiter, tokenList{i}];
end

% ====== Self demo
function selfdemo
tokenList={'This', 'is', 'a', 'test'};
str=feval(mfilename, tokenList, '-');
tokenList
fprintf('After running "str=split(tokenList, ''-'')":\n');
str