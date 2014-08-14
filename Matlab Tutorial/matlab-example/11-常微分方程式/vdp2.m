function dy = vdp2(t, y)
mu = 1000;
dy = [y(2); mu*(1-y(1)^2)*y(2)-y(1)];
