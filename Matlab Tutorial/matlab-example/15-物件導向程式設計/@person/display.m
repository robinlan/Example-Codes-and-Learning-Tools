function display(p)
% PERSON/DISPLAY Display of a person

out = '';
out = [out, '姓名 = ', p.name];
out = [out, ', 性別 = ', p.gender];
if p.height>0
	out = [out, ', 身高 = ', num2str(p.height) ' m'];
end
if p.weight>0
	out = [out, ', 體重 = ', num2str(p.weight) ' kg'];
end

disp([inputname(1), ': ', out])