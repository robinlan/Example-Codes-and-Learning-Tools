load census.mat				% 載入人口資料
theta = polyfit(cdate, pop, 2);		% 進行二次多項式擬合，找出 theta 值
fprintf('在2000年的預測值 = %g （百萬人）\n', polyval(theta, 2000));
fprintf('在2010年的預測值 = %g （百萬人）\n', polyval(theta, 2010));