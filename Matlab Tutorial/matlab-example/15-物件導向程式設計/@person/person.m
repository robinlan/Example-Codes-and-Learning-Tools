function p = person(name, gender, height, weight)
%PERSON Person class constructor

if isa(name, 'person')		% 若 vec 已經是 person 物件，則直接設定成輸出
	p = name;
else
	p.name = name;
	p.gender = gender;
	if nargin>=3, p.height=height; end
	if nargin>=4, p.weight=weight; end
	
	p = class(p, 'person');	% 將 p 加持成 person 物件
end
