clear
x = randn(5,1);        % A random vector of length 5
h = [1 1 1 1]/4;       % Length 4 averaging filter
d = gcd(48000,44100);
p = 48000/d;
q = 44100/d;
y = upfirdn(x,h,p,q);
