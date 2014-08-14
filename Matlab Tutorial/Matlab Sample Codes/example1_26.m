clear
b = [-4 8];
a = [1 6 8];
[r,p,k] = residuez(b,a)
imp = [1 0 0 0 0];
resptf = filter(b,a,imp)
respres = filter(r(1),[1 -p(1)],imp) + filter(r(2),[1 -p(2)],imp)