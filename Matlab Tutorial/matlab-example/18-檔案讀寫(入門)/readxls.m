fprintf('Show content in Sheet1(default):\n');
fprintf('A = xlsread(''1.xls'')')
A = xlsread('1.xls')
fprintf('Show content in Sheet2(captital does matter):\n');
fprintf('A = xlsread(''1.xls'', ''Sheet2'')')
B = xlsread('1.xls', 'Sheet2')

[C, D] = xlsread('1.xls', 'Sheet3')