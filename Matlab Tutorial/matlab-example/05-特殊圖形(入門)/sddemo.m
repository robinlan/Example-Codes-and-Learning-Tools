%gradientDescentDemo: Interactive demo of Gradient descent paths on "peaks" surface
%
%	Usage:
%		gradientDescentDemo
%
%	Description:
%		gradientDescentDemo gives interactive demo of gradient descent paths on "peaks" surface
%
%	Example:
%		gradientDescentDemo

%	Category: Demos
%	Roger Jang, 19961016, 20080801

%figure('name', 'Gradient Descent', 'NumberTitle', 'off');
subplot(1,2,1);
x=-3:.1:3;
y=-3:.1:3;
[xx,yy]=meshgrid(x,y);
zz=peaks(xx,yy);
surf(xx, yy, zz);
axis([-inf inf -inf inf -inf inf]);
colormap((jet+white)/2);
ball3dH = line(nan, nan, nan, 'marker', '.', 'markersize', 10, 'erase', 'none', 'color', 'k');
segment3dH = line(nan, nan, nan, 'erase', 'none', 'color', 'r');
set(gca, 'box', 'on');

%figure('name', 'Gradient Descent', 'NumberTitle', 'off');
subplot(1,2,2);
h=pcolor(xx,yy,zz);
hold on
colormap((jet+white)/2);
[junk, contourH] = contour(xx, yy, zz, 20, 'k-');   % Why the contours are missing???
shading interp;
%[px,py]=gradient(zz,.2,.2);
%quiver(x,y,-px,-py,2,'k');
hold off;
axis([-3 3 -3 3]); axis square;
ball2dH = line(nan, nan, 'marker', '.', 'markersize', 10, 'erase', 'none', 'color', 'k');

segment2dH = line([nan nan], [nan nan], 'erase', 'none', 'color', 'r');
title('Click to see gradient-descent paths');

AxisH = gca; FigH = gcf;
obj_fcn = 'peaksFunc';
eta = 0.1;

% The following is for animation
% action when button is first pushed down
% This action draws GD path on 2D plot only
action1a = ['curr_info=get(AxisH, ''currentPoint'');', ...
	'x=curr_info(1,1);y=curr_info(1,2);z=feval(obj_fcn,x,y);', ...
	'set(ball2dH,''xdata'',x,''ydata'',y);', ...
	'set(ball3dH,''xdata'',x,''ydata'',y,''zdata'',z);', ...
	'for i=1:100,', ...
	'grad=feval(obj_fcn,x,y,1);', ...
	'tmp=-grad/norm(grad);', ...
	'new_x=x+eta*tmp(1);', ...
	'new_y=y+eta*tmp(2);', ...
	'set(segment2dH,''xdata'',[x,new_x],''ydata'',[y,new_y]);', ...
	'x = new_x;', ...
	'y = new_y;', ...
	'end'];
% This action draws GD path on 3D surface too
action1b = ['curr_info=get(AxisH, ''currentPoint'');', ...
	'x=curr_info(1,1);y=curr_info(1,2);z=feval(obj_fcn,x,y);', ...
	'set(ball2dH,''xdata'',x,''ydata'',y);', ...
	'set(ball3dH,''xdata'',x,''ydata'',y,''zdata'',z);', ...
	'for i=1:100,', ...
	'grad=feval(obj_fcn,x,y,1);', ...
	'tmp=-grad/norm(grad);', ...
	'new_x=x+eta*tmp(1);', ...
	'new_y=y+eta*tmp(2);', ...
	'new_z=feval(obj_fcn,new_x,new_y);', ...
	'set(segment2dH,''xdata'',[x,new_x],''ydata'',[y,new_y]);', ...
	'set(segment3dH,''xdata'',[x,new_x],''ydata'',[y,new_y],''zdata'',[z,new_z]);', ...
	'x = new_x;', ...
	'y = new_y;', ...
	'z = new_z;', ...
	'end'];
action1 = action1b;

% actions after the mouse is pushed down
action2 = action1;
% action when button is released
action3 = action1;

% temporary storage for the recall in the down_action
set(AxisH,'UserData',action2);

% set action when the mouse is pushed down
down_action=['set(FigH,''WindowButtonMotionFcn'',get(AxisH,''UserData''));', action1];
set(FigH,'WindowButtonDownFcn',down_action);

% set action when the mouse is released
up_action=['set(FigH,''WindowButtonMotionFcn'','' '');', action3];
set(FigH,'WindowButtonUpFcn',up_action);
