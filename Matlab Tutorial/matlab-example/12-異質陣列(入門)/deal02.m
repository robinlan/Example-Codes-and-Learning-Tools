dirInfo = dir(matlabroot);			%　列出 MATLAB 的根目錄的各種資訊
n = length(dirInfo);				% 檔案及目錄的個數
[fileAndDir{1:n}] = deal(dirInfo.name);		% fileAndDir 包含各檔案及目錄名稱
dirs = fileAndDir([dirInfo.isdir])		% dirs 包含各目錄名稱