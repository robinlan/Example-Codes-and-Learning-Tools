nodes = [0 1 2 2 4 4 4 1 8 8 10 10 11 11 11 11];
treeplot(nodes);
h=findobj(0, 'type', 'line');	% h(1) is links; h(2) is nodes.
xdata=get(h(2), 'xdata');
ydata=get(h(2), 'ydata');
for i=1:length(nodes)
	text(xdata(i), ydata(i), ['  ', int2str(i)]);
end