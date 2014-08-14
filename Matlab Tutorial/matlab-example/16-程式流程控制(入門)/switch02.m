month = {'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Dec', 'Nov', 'Dec'};
for i = 1:length(month)
	switch month{i}
		case {'Mar','Apr','May'}
			season = 'Spring';
		case {'Jun','Jul','Aug'}
			season = 'Summer';
		case {'Sep','Oct','Nov'}
			season = 'Autumn';
		case {'Dec','Jan','Feb'}
			season = 'Winter';
	end
	fprintf('%s is %s.\n', month{i}, season);
end