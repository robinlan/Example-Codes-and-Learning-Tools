A = [3 5; 4 7; 2 1; 0 3];
[U, S, V] = svd(A)
maxDiff = max(max(abs(A-U*S*V')))