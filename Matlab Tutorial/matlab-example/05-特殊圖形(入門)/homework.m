theta = -pi:0.05:pi;
x = cos(theta);
y = sin(theta);
z = 0.5+abs(cos(5*theta)).*exp(abs(theta)/3);
stem3(x, y, z, 'fill');
view(-60, 50);
xlabel('X-axis');
ylabel('Y-axis');

figure;
z = 1+abs(sin(4*theta)./theta);
stem3(x, y, z, 'fill');