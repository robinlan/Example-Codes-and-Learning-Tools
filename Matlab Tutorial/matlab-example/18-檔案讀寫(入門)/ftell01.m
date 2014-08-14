fid = fopen('test.bin', 'w');
count = fwrite(fid, randperm(10), 'int32');
ftell(fid)