fid = fopen('test2.txt', 'r');
myData = fread(fid, 1, 'short')
fclose(fid);
myData2 = bin2dec([dec2bin(abs('h'),8), dec2bin(abs('T'),8)])