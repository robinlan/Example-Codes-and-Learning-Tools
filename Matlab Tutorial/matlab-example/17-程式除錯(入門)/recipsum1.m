function out = recipsum(x)

recip = reciproc(x);
out = sum(recip);

function output = reciproc(input)
output = 1./input;