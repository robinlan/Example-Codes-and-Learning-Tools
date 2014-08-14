x = linspace(0, 2*pi);		% 在 0 到 2π 間，等分取 100 個點  
plot(x, sin(x), 'o', x, cos(x), 'x', x, sin(x)+cos(x), '*');  