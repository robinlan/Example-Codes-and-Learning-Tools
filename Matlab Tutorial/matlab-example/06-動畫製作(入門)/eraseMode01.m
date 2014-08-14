t=linspace(0, 4*pi);
h1=plot(t, sin(t), 'lineWidth', 10, 'color', 'b');
hold on
h2=plot(t, cos(t), 'lineWidth', 20, 'color', 'r');
hold off

tic
set(h2, 'eraseMode', 'normal');
for i=1:1000
	yData=sin(t-i/100);
	set(h2, 'yData', yData);
	drawnow;
end
toc


tic
set(h2, 'eraseMode', 'xor');
for i=1:1000
	yData=sin(t-i/100);
	set(h2, 'yData', yData);
	drawnow;
end
toc

tic
set(h2, 'eraseMode', 'background');
for i=1:1000
	yData=sin(t-i/100);
	set(h2, 'yData', yData);
	drawnow;
end
toc

tic
set(h2, 'eraseMode', 'none');
for i=1:1000
	yData=sin(t-i/100);
	set(h2, 'yData', yData);
	drawnow;
end
toc
