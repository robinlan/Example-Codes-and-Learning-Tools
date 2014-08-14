function struct2html(struct)
% struct2html: Display a structure in HTML page
%	Usage: struct2html(struct)

%	Roger Jang, 20041001

if nargin<1; selfdemo; return; end
outputFile=[tempname, '.htm'];
fid=fopen(outputFile, 'w');

fprintf(fid, '<html><body>\n');
fprintf(fid, '<head>\n');
fprintf(fid, '<meta HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=big5">\n');
fprintf(fid, '</head>\n');
fprintf(fid, '<table border=1>');

fieldNames=fieldnames(struct);
fprintf(fid, '<tr><th>編號');
for i=1:length(fieldNames)
	fprintf(fid, '<th>%s', fieldNames{i});
end

for i=1:length(struct)
	fprintf(fid, '<tr><td>%d', i);
	for j=1:length(fieldNames)
		data=getfield(struct, {i}, fieldNames{j});
		if isstr(data)
			fprintf(fid, '<td>%s', data);
		elseif iscell(data)
			fprintf(fid, '<td>%s', cell2str(data));
		else
			fprintf(fid, '<td>%s', mat2str(data));
		end
	end
end

fprintf(fid, '</table>\n');
fprintf(fid, '</body></html>\n');
fclose(fid);

dos(['start ', outputFile]);

% ====== Self demo
function selfdemo
feval(mfilename, dir);