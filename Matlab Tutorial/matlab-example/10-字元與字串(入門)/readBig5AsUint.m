file='big5.txt';
fid=fopen(file, 'r');
contents=fread(fid, inf, 'uint8')';
fclose(fid);
disp(contents);
disp(native2unicode(contents));