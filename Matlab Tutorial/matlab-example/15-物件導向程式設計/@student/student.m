function s = student(name, gender, height, weight, department, year)
%STUDENT Student class constructor

p = person(name, gender, height, weight);	% Person class
s.department = department;	% student 特有的性質
s.year = year;			% student 特有的性質
s = class(s, 'student', p);	% 定義 s 為 student 物件，且繼承自 p 的類別