n = 1;
while prod(1:n) < 1e100
	n = n+1
end
fprintf('%g! = %e > 1e100\n', n, prod(1:n));