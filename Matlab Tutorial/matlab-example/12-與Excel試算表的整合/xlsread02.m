xlsFile = 'test01.xls';
B = xlsread(xlsFile, 'Sheet2')		% 讀出 'Sheet2' 的全部資料
C = xlsread(xlsFile, 2, 'A2:B4')	% 讀出第二個試算表位於 A2:B4 的資料