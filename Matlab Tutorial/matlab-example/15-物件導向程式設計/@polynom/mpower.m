function q = mpower(p, n)
% POLYNOM/MPOWER Implement p^n for polynoms.
if n<0 || n-round(n)~=0
	error('n must be a positive integer or zero!');
end

q = 1;
for i=1:n
	q=q*p;
end