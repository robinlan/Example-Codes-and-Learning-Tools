function output = fact01(n)
% FACT01 Calculate factorial of a given positive integer (for-loop version)

output = 1;
for i = 1:n,
	output = output*i;
end