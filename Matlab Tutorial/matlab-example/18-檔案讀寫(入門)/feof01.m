fid = fopen('test.txt');
A = fscanf(fid, '%g', [3 4])
feof(fid)