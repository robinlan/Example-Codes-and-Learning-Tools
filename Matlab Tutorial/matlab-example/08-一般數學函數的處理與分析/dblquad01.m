xMin = pi;
xMax = 2*pi;
yMin = 0;
yMax = pi;
result = dblquad(@integrand, xMin, xMax, yMin, yMax)