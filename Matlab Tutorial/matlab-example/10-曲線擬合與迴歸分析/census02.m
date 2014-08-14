load census.mat				% 更戈
A = [ones(size(cdate)), cdate, cdate.^2];
theta = A\pop;				% ノオ埃т程ㄎ theta 
t=2000; pop2000 = [1, t, t^2]*theta;	%  2000 瓣絬计箇代
t=2010; pop2010 = [1, t, t^2]*theta;	%  2010 瓣絬计箇代
fprintf('瓣2000箇代 = %g κ窾\n', pop2000);
fprintf('瓣2010箇代 = %g κ窾\n', pop2010);