function display(team)
% TEAM/DISPLAY Display of a team
out = '';
out = [out, '名稱 = "', team.name, '", '];
out = [out, '項目 = "', team.event, '", '];
out = [out, '隊員 = ['];
for i=1:length(team.member)
	out = [out, personName(team.member(i)), ' '];
end
out = [out, ']'];

disp([inputname(1), ': ', out])