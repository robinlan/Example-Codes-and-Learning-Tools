p = polynom([1 -4 -1 4]);
range = [-1.2, 4.2];
subplot(3,1,1); plot(p, range);
p2 = polyder(p);
subplot(3,1,2); plot(p2, range);
p3 = polyder(p2);
subplot(3,1,3); plot(p3, range);
