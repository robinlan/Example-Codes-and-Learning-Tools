load seamount.mat
tri = delaunay(x, y);
trisurf(tri, x, y, z);  
axis tight; colorbar