fid = fopen('test2.txt', 'r');
myData = fread(fid, [2 3])
fclose(fid);