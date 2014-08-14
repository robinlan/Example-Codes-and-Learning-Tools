r = 0:0.1:1;
t = linspace(0, 2*pi, 101);
[rr, tt] = meshgrid(r, t);
[xx, yy] = pol2cart(tt, rr);
z = rr.*exp(j*tt);
ff = abs((z.^3-1).^(1/3));
surfc(xx, yy, ff);