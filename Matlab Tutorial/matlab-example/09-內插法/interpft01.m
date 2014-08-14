n = 11;						% Number of the original data points
factor = 4;					% Increase the data by this factor
x = linspace(0, 2*pi, n);
y = sin(x).*exp(-x/5);
xi = (0:factor*n-1)*(x(2)-x(1))/factor;
yi = interpft(y, factor*n);
plot(x, y, 'ro', xi, yi, '.-');
legend('Original', 'Curve by interpft');
