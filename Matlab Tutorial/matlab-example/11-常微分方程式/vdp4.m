function [output1, output2, output3] = vdp4(t, y, flag, mu)
if nargin < 4 | isempty(mu),
	mu = 1;
end
if strcmp(flag, '')
	output1 = [y(2); mu*(1-y(1)^2)*y(2)-y(1)];	% dy/dt
elseif strcmp(flag, 'init'),
	output1 = [0; 25];		% Time span
	output2 = [3; 3];		% Initial conditions
	output3 = odeset;		% ODE options
end
