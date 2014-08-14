function plot(p, range)
% POLYNOM/PLOT  PLOT(p) plots the polynom p.
if nargin<2
	range = max(abs(roots(p)))*[-1 1];
end
x = linspace(range(1), range(2));
y = polyval(p, x);
plot(x, y);
title(polyAsString(p))
grid on