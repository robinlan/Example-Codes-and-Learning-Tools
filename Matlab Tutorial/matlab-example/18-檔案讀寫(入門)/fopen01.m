[fid, message] = fopen('no_such_file', 'r');
fprintf('fid = %d\n', fid);
fprintf('message = %s\n', message);