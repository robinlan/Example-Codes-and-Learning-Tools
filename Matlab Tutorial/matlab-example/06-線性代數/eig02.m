A = magic(5);
[X, D] = eig(A)
maxDiff=max(max(abs(A-X*D*inv(X))))