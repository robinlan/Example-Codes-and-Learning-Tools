p = [1 2 1];
x = 0:0.1:3;
y = polyval(p, x);
plot(x, y, '-o');