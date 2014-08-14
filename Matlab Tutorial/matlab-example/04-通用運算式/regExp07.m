string = {'Barcelona', 'Y2K', 'MATLAB 7.3'};
pattern = {'lona', '[0-9]', '[A-Z]'};
[start, finish] = regexp(string, pattern)