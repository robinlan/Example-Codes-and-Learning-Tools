x = [0 2 4 3 1 2 1];  
y = [4 1 2 4 5 2 0];
index = 1:length(x);
index2 = linspace(1, length(x), 101);
x2 = interp1(index, x, index2, 'spline');
y2 = interp1(index, y, index2, 'spline');  
plot(x, y, 'o', x2, y2, '-');
legend('Origianl data', 'Interpolated data');