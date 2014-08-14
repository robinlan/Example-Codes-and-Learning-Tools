[x, y] = meshgrid(-2:0.1:2);  
z = y.*exp(-x.^2-y.^2);  
plot3(x, y, z); 