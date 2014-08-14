function dy = vdp1(t, y)
mu = 1;
dy = [y(2); mu*(1-y(1)^2)*y(2)-y(1)];
