theta = -pi:0.05:pi;
x = cos(theta);
y = sin(theta);
z = abs(cos(3*theta)).*exp(-abs(theta/2));
stem3(x, y, z);