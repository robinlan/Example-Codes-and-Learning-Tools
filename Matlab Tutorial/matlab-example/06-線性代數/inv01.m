A = pascal(4);		% 產生 4x4 的 Pascal 方陣
B = inv(A)
I1 = A*B
I2 = B*A
maxDiff=max(max(abs(eye(4)-I1)))