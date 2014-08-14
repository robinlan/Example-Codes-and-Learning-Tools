function out = recipsum(x)

recip = reciproc(x);
out = sum(recip);

function output = reciproc(input)
input(find(input==0)) = [];	% Eliminate "0" elements 
output = 1./input;
