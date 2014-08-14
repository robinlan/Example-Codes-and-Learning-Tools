options = odeset('OutputFcn', 'odephas3');	% 使用 odephas3 進行繪圖
ode45('lorenzOde', [0 10], [20 5 -5]', options);