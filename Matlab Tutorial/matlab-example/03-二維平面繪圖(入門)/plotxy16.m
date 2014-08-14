x = 0:0.1:2*pi;  
plot(x, sin(x), x, cos(x));   
text(pi/4, sin(pi/4),'\leftarrow sin(\pi/4) = 0.707');  
text(5*pi/4, cos(5*pi/4),'cos(5\pi/4) = -0.707\rightarrow', 'HorizontalAlignment', 'right');  
