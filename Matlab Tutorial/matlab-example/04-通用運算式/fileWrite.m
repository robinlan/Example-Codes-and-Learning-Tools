function filewrite(cellStr, fileName);
%FILEWRITE Write a cell string to a file

%	Roger Jang, 20020618

if nargin==0, selfdemo; return; end

fid = fopen(fileName, 'w');
if fid<0,
	errorMessage=['Cannot open "', fileName, '"!'];
	error(errorMessage);
end

for i=1:length(cellStr),
	fprintf(fid, '%s\r\n', cellStr{i});
end

fclose(fid);

% ====== self demo
function selfdemo
fileName = [tempdir, 'test.txt'];
fprintf('fileName=%s\n', fileName);
cellStr={'This', 'is', 'a', 'test.'};
fprintf('cellStr = ');
disp(cellStr);
feval(mfilename, cellStr, fileName);
fprintf('The contents of "cellStr" have been written to "%s":\n', fileName);
type(fileName);