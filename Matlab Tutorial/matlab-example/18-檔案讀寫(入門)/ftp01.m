ftpSite = 'ftp.mathworks.com';
tmw = ftp(ftpSite)
x = dir(tmw)
cd(tmw, 'pub/books/jang');
fileName = 'README';
fprintf('Getting %s from %s...\n', fileName, ftpSite);
mget(tmw, fileName);