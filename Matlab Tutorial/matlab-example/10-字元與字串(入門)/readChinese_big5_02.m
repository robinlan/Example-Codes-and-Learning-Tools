file='big5.txt';
fid=fopen(file, 'r');
contents=fread(fid, inf, 'char')';
fclose(fid);
disp(contents);
disp(char(contents));
