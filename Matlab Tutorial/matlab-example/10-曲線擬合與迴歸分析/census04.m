load census.mat				% 載入人口資料
meanYear=mean(cdate);
sigmaYear=sqrt(var(cdate));
color=hsv(8);
year=(min(cdate):2020);
yearScaled=(year-meanYear)/sigmaYear;
cdateScaled=(cdate-meanYear)/sigmaYear;
plot(t,p,'bo');
plot(cdate, pop, 'bo'); hold on
legendStr={'data'};
for order=1:8
	theta = polyfit(cdateScaled, pop, order);	% 進行多項式擬合，找出 theta 值
	plot(year, polyval(theta, yearScaled), 'color', color(order,:));
	legendStr{end+1} = ['Order=' int2str(order)];
end
hold off; axis tight
legend(legendStr, 2);
title('Population of the US');
xlabel('Years');
ylabel('Millions');
