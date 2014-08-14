t = (1/16:1/8:1)*2*pi;
x = cos(t);
y = sin(t);
c = linspace(0, 1, length(t));
fill3(x, y/sqrt(2), y/sqrt(2), c, x/sqrt(2), y, x/sqrt(2), c);
colorbar;
axis tight; box on;