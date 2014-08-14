xlsFile = 'output03.xls';
sheetName='位置對時間的變化';
data={'時間 (sec)', '位置 (m)'};
for i=1:5
	data{i+1,1}=i;
	data{i+1,2}=0.5*9.8*i^2;
end
[status, message] = xlswrite(xlsFile, data, sheetName);
dos(['start ' xlsFile]);