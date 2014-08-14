x = rand(20,1);
y = rand(20,1);
voronoi(x, y, 'b:');
tri = delaunay(x, y);
hold on
h = trimesh(tri, x, y, 0*x);
hold off
set(h, 'facecolor', 'none');
axis equal square
axis([0 1 0 1]);