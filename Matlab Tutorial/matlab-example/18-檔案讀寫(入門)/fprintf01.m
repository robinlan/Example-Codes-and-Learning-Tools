x = 1:10;
y = [x; sqrt(x)];
fid = fopen('squareRootTable.txt', 'w');
fprintf(fid, 'Table of square root:\r\n');
fprintf(fid, '%2.0f => %10.6f\r\n', y);
fclose(fid);
dos('start squareRootTable.txt');	% ¶}±Ò squareRootTable.txt