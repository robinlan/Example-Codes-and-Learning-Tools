x = 0:0.1:4*pi;  
plot(x, sin(x)+sin(3*x))   
set(gca, 'ytick', [-1 -0.3 0.1 1]);	% 在 y 軸加上格線點
grid on					% 加上格線 
