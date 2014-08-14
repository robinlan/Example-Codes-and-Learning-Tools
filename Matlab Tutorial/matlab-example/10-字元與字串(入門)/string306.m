input_string = 'ee cs econ stat me';  
remainder = input_string;  
parsed = '';		% 建立一空字元陣列  
while (any(remainder))  
	[chopped, remainder] = strtok(remainder);  
	parsed = strvcat(parsed, chopped);  
end  
parsed