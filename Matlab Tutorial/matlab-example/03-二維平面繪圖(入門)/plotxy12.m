x = 0:0.1:4*pi;  
plot(x, sin(x)+sin(3*x))  
set(gca, 'ytick', [-1 -0.3 0.1 1])  
set(gca, 'yticklabel', {'極小','臨界值','崩潰值','極大'})  
grid on						% 加上格線

