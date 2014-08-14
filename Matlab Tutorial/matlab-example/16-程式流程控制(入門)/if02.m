y = [0 3 4 1 6];
for i = 1:length(y)
	if rem(y(i), 2)==0
		fprintf('y(%g) = %g is even.\n', i, y(i));
	else
		fprintf('y(%g) = %g is odd.\n', i, y(i));
	end
end