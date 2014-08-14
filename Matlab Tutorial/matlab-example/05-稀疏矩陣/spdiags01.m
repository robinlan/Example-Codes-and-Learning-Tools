D = [2 7; 4 9; 1 3];
d = [1 -1];
S = spdiags(D, d, 4, 3)
A = full(S)