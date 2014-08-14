theta = linspace(0, 2*pi, 7);
xv = cos(theta);
yv = sin(theta);
x = randn(250,1); y = randn(250,1);
in = inpolygon(x, y, xv, yv);
plot(xv, yv, 'b', x(in), y(in), 'g.', x(~in), y(~in),'k.');
axis image