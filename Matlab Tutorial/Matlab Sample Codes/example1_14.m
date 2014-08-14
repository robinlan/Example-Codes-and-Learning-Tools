clear
b = 1;                                   % Numerator
a = [1 -0.9];                            % Denominator
x1 = randn(5000,1);                     % Generate two random data sequences.
x2 = randn(5000,1);
[y1,zf] = filter(b,a,x1);
y2 = filter(b,a,x2,zf);
zf = filtic(b,a,flipud(y1),flipud(x1));