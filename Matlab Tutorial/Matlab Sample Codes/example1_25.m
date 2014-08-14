clear
b = [2 3 4];
a = [1 3 3 1];
q = roots(b)
p = roots(a)
k = b(1)/a(1)
bb = k*poly(q)
aa = poly(p)