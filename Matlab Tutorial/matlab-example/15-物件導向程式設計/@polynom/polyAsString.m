function s = polyAsString(poly)
% POLYNOM/POLYASSTRING String representation of a polynom
degree=length(poly.c)-1;
s = sprintf('%d*x^%d', poly.c(1), degree);
for i=degree-1:-1:0
	coef = poly.c(degree-i+1);
	if coef >=0 
		s=sprintf('%s + %d*x^%d', s, coef, i);
	else
		s=sprintf('%s - %d*x^%d', s, -coef, i);
	end
end