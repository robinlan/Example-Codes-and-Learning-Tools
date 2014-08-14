x = 0:0.1:8*pi;
for i = 1:5000
    plot(x, sin(x-i/50).*exp(-x/5));
    axis([-inf inf -1 1]);
    grid on
    drawnow
end
