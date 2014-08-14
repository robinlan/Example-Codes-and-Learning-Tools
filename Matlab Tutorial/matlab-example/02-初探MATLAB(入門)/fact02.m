function output = fact02(n)
% FACT2 Calculate factorial of a given positive integer (recursive version)

if n == 1,	% Terminating condition
	output = 1;
	return;
end

output = n*fact02(n-1);