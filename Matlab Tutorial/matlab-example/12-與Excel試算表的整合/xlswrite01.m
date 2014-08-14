xlsFile = 'output01.xls';
xlswrite(xlsFile, randn(5));
dos(['start ' xlsFile]);