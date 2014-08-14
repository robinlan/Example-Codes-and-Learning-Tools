[x, y, z] = peaks(20);
[u, v] = gradient(z);
contour(x, y, z, 10);
hold on, quiver(x,y,u,v); hold off
axis image