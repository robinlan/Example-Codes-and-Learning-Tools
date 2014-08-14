function t = team(name, event, person1, person2, person3, person4)
%TEAM team class constructor
t.name = name;
t.event = event;
t.member(1) = person1;
t.member(2) = person2;
t.member(3) = person3;
t.member(4) = person4;
t = class(t, 'team');	% 將 t 加持成 team 物件