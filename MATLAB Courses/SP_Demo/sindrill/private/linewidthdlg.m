function linewidth = linewidthdlg(action)
%LINEWDITHDLG Line width dialog box.
%  w = LINEWIDTHDLG creates a modal dialog box that returns the width
%  of the line selected in the dialog box.
%
%  w = LINEWIDTHDLG(x) uses the default width of x.  x must be greater than
%  or equal to .5

% Jordan Rosenthal, 12/14/97
% Revision 2, 4/6/98

if nargin == 1 & isstr(action)
   % Perform action
   switch action
   case 'SetWidth'
      hLine = findobj(gcbf,'Type','line');
      SliderValue = get(findobj(gcbf,'Style','slider'), 'Value');
      set(hLine, 'LineWidth', SliderValue);
   case 'OK'
      set(gcbf,'UserData',1);
   otherwise
      error('Illegal action');
   end
elseif nargin == 0 | ~isstr(action)
   if nargin == 0
      DefLineWidth = get(0,'DefaultlineLineWidth');
   elseif action >= .5
      DefLineWidth = action;
   else
      error('Illegal value for default line width.');
   end
   % Setup Dialog
   OldUnits = get(0, 'Units');
   set(0, 'Units','pixels');
   ScreenSize = get(0,'ScreenSize');
   set(0, 'Units', OldUnits);
   DlgPos = [0.35*ScreenSize(3), 0.325*ScreenSize(4), 0.3*ScreenSize(3), 0.35*ScreenSize(4)];
   hDlg = dialog( ...
      'Name','Line Thickness', ...
      'CloseRequestFcn','linewidthdlg OK', ...
      'Position',DlgPos, ...
      'UserData',0);
   
   % Setup Axis
   hAxes = axes('Parent',hDlg, ...
      'NextPlot','Add', ...
      'Position',[0.2 0.4 0.6 0.5], ...
      'XTick',[], ...
      'YTick',[]);
   
   % Setup Line
   plot([0 0],[0 1],'-','Parent',hAxes,'Erasemode','Xor','LineWidth',DefLineWidth);
   plot(0,1,'o','Parent',hAxes,'Erasemode','Xor','LineWidth',DefLineWidth);
   
   % Setup Slider
   hSlider = uicontrol('Parent',hDlg, ...
      'Units','Normalized', ...
      'Callback','linewidthdlg SetWidth', ...
      'Min',0.5, ...
      'Max',5.5, ...
      'Position',[0.2 0.3 0.6 0.1], ...
      'SliderStep',[0.1 0.2], ...
      'Style','slider', ...
      'Value',DefLineWidth );
   
   % Setup OK Button
   uicontrol('Parent',hDlg, ...
      'Units','normalized', ...
      'Callback','linewidthdlg OK', ...
      'FontWeight','Bold', ...
      'Position',[0.375 0.1 0.25 0.1], ...
      'String','OK', ...
      'Style','pushbutton');
   
   % Wait for user to hit OK and return result
   % For some reason I can't use just waitfor() and uiresume() when this
   % function is in a private directory.
   waitfor(hDlg,'UserData');
   linewidth = get(hSlider, 'Value');
   delete(hDlg);
else
   error('Too many input arguments.');
end
