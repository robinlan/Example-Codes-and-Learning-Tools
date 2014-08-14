x = 0:0.1:2*pi;  
y = sin(x);  
plot(x,y)   
str = ['\leftarrow (', num2str(2.5), ', ', num2str(sin(2.5)),  ')'];   
text(2.5, sin(2.5), str)