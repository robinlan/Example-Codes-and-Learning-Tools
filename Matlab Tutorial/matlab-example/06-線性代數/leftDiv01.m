A = vander(1:3);
b = [6; 11; 18];
x = A\b
error = A*x-b