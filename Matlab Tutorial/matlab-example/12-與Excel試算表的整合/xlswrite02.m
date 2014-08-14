xlsFile = 'output02.xls';
sheetName='7x7魔方陣';
[status, message] = xlswrite(xlsFile, magic(7), sheetName)
xlswrite(xlsFile, {'以上是7x7魔方陣'; date}, sheetName, 'B8:B9');
dos(['start ' xlsFile]);