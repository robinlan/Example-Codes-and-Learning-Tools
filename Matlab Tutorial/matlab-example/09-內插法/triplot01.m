load seamount.mat
tri = delaunay(x, y);   
triplot(tri, x, y);
hold on, plot(x, y, '.r'); hold off