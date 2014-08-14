A = [1 2 3; 4 5 6];
m = size(A, 1);
n = size(A, 2);
M = 3;
N = 2;
mIndex = (1:m)'*ones(1,M);
nIndex = (1:n)'*ones(1,N);
B = A(mIndex, nIndex)