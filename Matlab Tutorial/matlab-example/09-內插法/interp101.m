x = 0:1:4*pi;  
y = sin(x).*exp(-x/5);  
xi = 0:0.1:4*pi;  
y1 = interp1(x, y, xi, 'nearest');  
y2 = interp1(x, y, xi, 'linear');  
y3 = interp1(x, y, xi, 'pchip');  
y4 = interp1(x, y, xi, 'spline');  
plot(x, y, 'o', xi, y1, xi, y2, xi, y3, xi, y4);  
legend('Original', 'Nearest', 'Linear', 'Pchip', 'Spline');