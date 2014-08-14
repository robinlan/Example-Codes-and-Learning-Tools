warning off			% 刪除此列將可顯示警告訊息
load census.mat
cdate2 = min(cdate):(max(cdate)+30);
curve = zeros(7, length(cdate2));
for i = 1:7
	curve(i,:) = polyval(polyfit(cdate,pop,i), cdate2);
end
plot(cdate, pop,'o', cdate2, curve);
legend('實際資料', 'order=1', 'order=2','order=3', 'order=4', 'order=5', 'order=6', 'order=7');
xlabel('年份');
ylabel('人口（單位：百萬）');