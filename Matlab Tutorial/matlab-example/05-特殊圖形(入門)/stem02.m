t = 0:0.2:4*pi;
y = cos(t).*exp(-t/5);
stem(t, y, 'fill');