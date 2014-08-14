r=0:0.2:4;
t=linspace(0, 2*pi, 50);
[rr, tt]=meshgrid(r, t);
xx=rr.*cos(tt);
yy=rr.*sin(tt);
ff=peaks(xx,yy);
surf(xx, yy, ff);
axis tight