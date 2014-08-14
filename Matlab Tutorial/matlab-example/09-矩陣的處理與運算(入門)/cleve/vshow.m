function vshow()
%VSHOW  Investigate Voronoy diagrams and Delaunay triangularizations.

load vorofig

a = figure('Color',[0.752941 0.752941 0.752941], ...
	'Colormap',mat0, ...
	'Name','Voronoi Diagrams', ...
	'NumberTitle','off', ...
	'PointerShapeCData',mat1, ...
        'Renderer','painters', ...
	'Tag','Fig4', ...
	'UserData',mat2);
b = uicontrol('Parent',a, ...
	'Units','normalized', ...
	'BackgroundColor',[0.752941 0.752941 0.752941], ...
	'ListboxTop',0, ...
	'Position',[0.702341 0.047619 0.267559 0.428571], ...
	'Style','frame', ...
	'Tag','Frame1');
b = axes('Parent',a, ...
	'Box','on', ...
	'CameraUpVector',[0 1 0], ...
	'Color',[1 1 1], ...
	'ColorOrder',mat3, ...
	'DataAspectRatioMode','manual', ...
	'PlotBoxAspectRatio',[33.1605 33.288 1], ...
	'PlotBoxAspectRatioMode','manual', ...
	'Position',[0.0501672 0.0952381 0.618729 0.788095], ...
	'WarpToFill','off', ...
	'XColor',[0 0 0], ...
	'XLim',[-32.9045 33.4165], ...
	'XLimMode','manual', ...
	'XTickMode','manual', ...
	'YColor',[0 0 0], ...
	'YLim',[-33.2865 33.2895], ...
	'YLimMode','manual', ...
	'YTickMode','manual', ...
	'ZColor',[0 0 0]);
c = text('Parent',b, ...
	'Color',[0 0 0], ...
	'HandleVisibility','callback', ...
	'HorizontalAlignment','center', ...
	'Position',[0.154815 34.706 0], ...
	'String','Click and drag the points', ...
	'VerticalAlignment','bottom');
set(get(c,'Parent'),'Title',c);
c = text('Parent',b, ...
	'Color',[0 0 0], ...
	'HandleVisibility','callback', ...
	'HorizontalAlignment','center', ...
	'Position',[0.154815 -34.9054 0], ...
	'VerticalAlignment','cap');
set(get(c,'Parent'),'XLabel',c);
c = text('Parent',b, ...
	'Color',[0 0 0], ...
	'HandleVisibility','callback', ...
	'HorizontalAlignment','center', ...
	'Position',[-34.2461 -0.302046 0], ...
	'Rotation',90, ...
	'VerticalAlignment','baseline');
set(get(c,'Parent'),'YLabel',c);
c = text('Parent',b, ...
	'Color',[0 0 0], ...
	'HandleVisibility','callback', ...
	'HorizontalAlignment','right', ...
	'Position',[-43.3523 42.8003 0], ...
	'Visible','off');
set(get(c,'Parent'),'ZLabel',c);
b = uicontrol('Parent',a, ...
	'Units','normalized', ...
	'Callback','vorodrag initguirand', ...
	'ListboxTop',0, ...
	'Position',[0.719064 0.642857 0.234114 0.119048], ...
	'String','Random Points', ...
	'Tag','Pushbutton1');
b = uicontrol('Parent',a, ...
	'Units','normalized', ...
	'Callback','vorodrag initguisunflower', ...
	'ListboxTop',0, ...
	'Position',[0.719064 0.333333 0.234114 0.119048], ...
	'String','Spiral Points', ...
	'Tag','Pushbutton1');
b = uicontrol('Parent',a, ...
	'Units','normalized', ...
	'BackgroundColor',[1 1 1], ...
	'Callback','vorodrag plot', ...
	'ListboxTop',0, ...
	'Position',[0.874582 0.880952 0.083612 0.047619], ...
	'String','30', ...
	'Style','edit', ...
	'Tag','npts');
b = uicontrol('Parent',a, ...
	'Units','normalized', ...
	'BackgroundColor',[0.752941 0.752941 0.752941], ...
	'ListboxTop',0, ...
	'Position',[0.719064 0.880952 0.150502 0.047619], ...
	'String','Number', ...
	'Style','text', ...
	'Tag','StaticText1');
b = uicontrol('Parent',a, ...
	'Units','normalized', ...
	'BackgroundColor',[0.752941 0.752941 0.752941], ...
	'Callback','if get(gcbo,''Value'')==1,vorodrag delaunay,else,vorodrag plot,end', ...
	'ListboxTop',0, ...
	'Position',[0.719064 0.795238 0.234114 0.0714286], ...
	'String','Delaunay triangles', ...
	'Style','checkbox', ...
	'Tag','delaunaycheckbox');
b = uicontrol('Parent',a, ...
  	'Units','normalized', ...
  	'BackgroundColor',[1 1 1], ...
  	'Callback','vorodrag initguisunflower', ...
  	'ListboxTop',0, ...
  	'Position',[79/100 1/6 7/40 0.0642857], ...
  	'String','?', ...
  	'Style','edit', ...
  	'Tag','f');
b = uicontrol('Parent',a, ...
	'Units','normalized', ...
	'Callback','vorodrag initguihex', ...
	'ListboxTop',0, ...
	'Position',[0.719064 0.5 0.234114 0.119048], ...
	'String','Hex Points', ...
	'Tag','Pushbutton1');
b = uicontrol('Parent',a, ...
	'Units','normalized', ...
	'BackgroundColor',[0.752941 0.752941 0.752941], ...
	'Callback','if get(gcbo,''Value'')==1,vorodrag fermat,else,vorodrag plot,end', ...
	'ListboxTop',0, ...
	'Position',[0.719064 0.238095 0.234114 0.0714286], ...
	'String','Fermat Spiral', ...
	'Style','checkbox', ...
	'Tag','fermatspiral');
b = uicontrol('Parent',a, ...
	'Units','normalized', ...
	'BackgroundColor',[0.752941 0.752941 0.752941], ...
	'ListboxTop',0, ...
	'Position',[7/10 1/6 8/100 0.047619], ...
	'String','f =', ...
	'Style','text', ...
	'Tag','StaticText1');
b = uicontrol('Parent',a, ...
	'Units','normalized', ...
	'BackgroundColor',[0.752941 0.752941 0.752941], ...
	'Callback',['if get(gcbo,''Value'')==1,' ...
                    'load vexamples, image(F), colormap(Fmap), axis off,'...
                    'else,vorodrag plot,end'], ...
	'ListboxTop',0, ...
	'Position',[0.72 0.09 0.234114 0.0714286], ...
	'String','Example 1', ...
	'Style','checkbox');
b = uicontrol('Parent',a, ...
	'Units','normalized', ...
	'BackgroundColor',[0.752941 0.752941 0.752941], ...
	'Callback',['if get(gcbo,''Value'')==1,' ...
                    'load vexamples, image(L), colormap(Lmap), axis off,'...
                    'else,vorodrag plot,end'], ...
	'ListboxTop',0, ...
	'Position',[0.72 0.045 0.234114 0.0714286], ...
	'String','Example 2', ...
	'Style','checkbox');
