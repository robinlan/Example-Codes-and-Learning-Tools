options = odeset('OutputSel', [1,3]);	% 只畫出第一和第三個狀態變數
ode45('lorenzOde', [0 10], [20 5 -5]', options);