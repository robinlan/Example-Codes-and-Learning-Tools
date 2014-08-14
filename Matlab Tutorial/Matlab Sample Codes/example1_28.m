clear
c = [1; 2; 3]; 
x = randn(3,1);
A = convmtx(c,3)
y1 = A*x
y2 = conv(c,x)