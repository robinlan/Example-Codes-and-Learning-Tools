[x, y] = meshgrid(-2:0.2:2, -1:0.1:1);
z = x.*exp(-x.^2-y.^2);
[u, v, w] = surfnorm(x, y, z);
quiver3(x, y, z, u, v, w);
hold on, surf(x, y, z); hold off
axis equal
%colormap('default')			% 顏色改回預設值 