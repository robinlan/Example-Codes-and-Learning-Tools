warning off			% 刪除此列將可顯示警告訊息
load census.mat
plot(cdate, pop, 'o');
p3 = polyfit(cdate, pop, 3);
cdate2 = 1790:2002;
pop2 = polyval(p3, cdate2);
plot(cdate, pop, 'o', cdate2, pop2, '-');
xlabel('年份');
ylabel('人口（單位：百萬）');
legend('實際人口', '預測人口');
popAt2002 = polyval(p3, 2002)