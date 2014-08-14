function out = func5(x)
recip = reciproc(x);
out = sum(recip);

% Definition for subfunctions
function output = reciproc(input)
output = 1./input;
