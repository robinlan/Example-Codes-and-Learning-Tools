function [q, r] = mldivide(a, b)
% POLYNOM/MLDIVIDE Implement a\b for polynoms.
[q, r] = mrdivide(b, a);