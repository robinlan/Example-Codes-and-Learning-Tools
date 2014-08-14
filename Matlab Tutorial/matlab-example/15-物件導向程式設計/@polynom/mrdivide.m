function [q, r] = mrdivide(a, b)
% POLYNOM/MRDIVIDE Implement a/b for polynoms.

a = polynom(a);
b = polynom(b);
[q, r] = deconv(a.c, b.c);
q = polynom(q);
r = polynom(r);