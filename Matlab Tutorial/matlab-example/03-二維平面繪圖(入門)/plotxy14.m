t = 0:0.1:2*pi;  
x = 3*cos(t);  
y = sin(t);  
subplot(2, 2, 1); plot(x, y); axis normal  
subplot(2, 2, 2); plot(x, y); axis square  
subplot(2, 2, 3); plot(x, y); axis equal  
subplot(2, 2, 4); plot(x, y); axis equal tight 
