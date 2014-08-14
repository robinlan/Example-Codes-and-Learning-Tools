y = [3 4 5 9 2];
for i = 1:length(y)
	if rem(y(i),3)==0
		fprintf('y(%g)=%g is 3n.\n', i, y(i));
	elseif rem(y(i), 3)==1
		fprintf('y(%g)=%g is 3n+1.\n', i , y(i));
	else
		fprintf('y(%g)=%g is 3n+2.\n', i , y(i));
	end
end