r = linspace(1, 2, 11);
t = linspace(0, 6*pi, 101);
[rr, tt] = meshgrid(r, t);
xx = rr.*cos(tt);
yy = rr.*sin(tt);
ff = tt;
surf(xx, yy, ff, rr);