subplot (2,1,1);
fplot(@(x)sin(1./x), [0.01,0.1]);
subplot (2,1,2);
fplot(@(x)sin(1./x), [0.01,0.1], 0.0001);