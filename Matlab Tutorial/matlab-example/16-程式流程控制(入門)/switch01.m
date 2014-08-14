for month = 1:12
	switch month
		case {3,4,5}
			season = 'Spring';
		case {6,7,8}
			season = 'Summer';
		case {9,10,11}
			season = 'Autumn';
		case {12,1,2}
			season = 'Winter';
	end
	fprintf('Month %d ===> %s.\n', month, season);
end

