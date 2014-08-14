clear
b = 1;                % Numerator
a = [1 -0.9];         % Denominator
x = randn(5,1)        % A random vector of length 5
y = filter(b,a,x)