for i = 1:1000
	if prod(1:i) > 1e100
		fprintf('%g! = %e > 1e100\n', i, prod(1:i));
		break;			% ¸õ¥X for °j°é
	end
end