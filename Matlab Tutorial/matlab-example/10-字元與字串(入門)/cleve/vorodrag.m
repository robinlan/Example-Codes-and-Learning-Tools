function vorodrag(action)
% For dragging voronoi diagrams

if nargin==0,
   action='init';
end

switch action
case 'gui',
   vshow
   vorodrag initguirand
   
case 'init',
   
   % Sunflower starting numbers
   k=1;
   f=2/(1+sqrt(5));
   d=f*2*pi;
   minCount=10;
   maxCount=40;
   theta1=((minCount:maxCount)*d)';
   r1=k*sqrt(theta1);
   ud.x=r1.*cos(theta1);
   ud.y=r1.*sin(theta1);
   
   % Random starting numbers
   %numpts=20;
   %ud.x=rand(numpts,1);
   %ud.y=rand(numpts,1);
   
   set(gcf,'UserData',ud);   
   LocalPlotPoints
   
case 'initguisunflower',
   % Sunflower starting numbers
   k=1;
   f=get(findobj(gcbf,'Tag','f'),'String');
   if isequal(f,'?')
      f=2/(1+sqrt(5));
   else
      f=eval(f);
   end
   d=f*2*pi;
   minCount=1;
   maxCount=eval(get(findobj(gcbf,'Tag','npts'),'String'));
   theta1=((minCount:maxCount)*d)';
   r1=k*sqrt(theta1);
   ud.x=r1.*cos(theta1);
   ud.y=r1.*sin(theta1);
   set(gcf,'UserData',ud);   
   LocalPlotPoints
      
case 'initguirand',
   % Random starting numbers
   numpts=eval(get(findobj(gcbf,'Tag','npts'),'String'));
   ud.x=rand(numpts,1);
   ud.y=rand(numpts,1);
   set(gcf,'UserData',ud);   
   LocalPlotPoints
   
case 'initguihex',
   % Random starting numbers
   %numpts=eval(get(findobj(gcbf,'Tag','npts'),'String'));
   [x1,y1]=meshgrid(1:2:8);
   [x2,y2]=meshgrid(2:2:8);
   %x2=[]; y2=[];
   ud.x=[x1(:); x2(:)];
   ud.y=[y1(:); y2(:)]*sqrt(3);
   set(gcf,'UserData',ud);   
   LocalPlotPoints
   
case 'mousedown',
   currPt=get(gca,'CurrentPoint');
   ud=get(gcf,'UserData');
   xc=currPt(1);
   yc=currPt(3);
   threshold=0.02;
   % Figure out what point has been clicked
   ax=axis;
   xthreshold=(ax(2)-ax(1))/30;
   ythreshold=(ax(2)-ax(1))/30;
   ud.ptIndex=intersect(find(abs((ud.x-xc))<xthreshold),find(abs((ud.y-yc))<ythreshold));
   ud.ptIndex=ud.ptIndex(1);
   set(gcf,'UserData',ud);
   set(gcbf,'WindowButtonMotionFcn','vorodrag drag');
   set(gcbf,'WindowButtonUpFcn','vorodrag mouseup');
   
case 'drag',
   currPt=get(gca,'CurrentPoint');
   ud=get(gcbf,'UserData');
   xc=currPt(1);
   yc=currPt(3);
   ud.x(ud.ptIndex)=xc;
   ud.y(ud.ptIndex)=yc;
   [vx,vy]=voronoi(ud.x,ud.y);
   vx=[vx; NaN*ones(1,size(vx,2))];
   vy=[vy; NaN*ones(1,size(vy,2))];
   set(ud.xyHndl,'XData',ud.x,'YData',ud.y);
   set(ud.vHndl,'XData',vx(:),'YData',vy(:));
   set(gcf,'UserData',ud);
   
case 'mouseup',
   set(gcbf,'WindowButtonMotionFcn','');
   set(gcbf,'WindowButtonUpFcn','');
   
case 'delaunay',
   LocalPlotDelaunay
   
case 'fermat',
   LocalPlotFermat
   
case 'plot',
   LocalPlotPoints
   
end

function LocalPlotPoints
set(findobj(gcf,'Tag','delaunaycheckbox'),'Value',0);
set(findobj(gcf,'Tag','fermatspiral'),'Value',0);
% Plot the points

ud=get(gcf,'UserData');

% Plot all the gridlines as a single line
[vx,vy]=voronoi(ud.x,ud.y);
vx=[vx; NaN*ones(1,size(vx,2))];
vy=[vy; NaN*ones(1,size(vy,2))];
cla
ud.xyHndl=plot(ud.x,ud.y,'Marker','.','MarkerSize',24,'LineStyle','none');
ud.vHndl=line(vx(:),vy(:));
axis equal
axis([min(ud.x) max(ud.x) min(ud.y) max(ud.y)])
set(gca,'XTick',[],'YTick',[]);
title('Click and drag the points');
set([ud.vHndl ud.xyHndl],'EraseMode','background');
set(ud.xyHndl,'ButtonDownFcn','vorodrag mousedown');
set(gcf,'UserData',ud);   

function LocalPlotDelaunay
% Plot the points

ud=get(gcf,'UserData');

% Plot all the gridlines as a single line
[vx,vy]=voronoi(ud.x,ud.y);
tri=delaunay(ud.x,ud.y);
vx=[vx; NaN*ones(1,size(vx,2))];
vy=[vy; NaN*ones(1,size(vy,2))];
cla
ud.xyHndl=plot(ud.x,ud.y,'Marker','.','MarkerSize',24, ...
  	'LineStyle','none');
ud.vHndl=line(vx(:),vy(:),'Color',[.9 .9 .9]);
hold on
triHndl=trimesh(tri,ud.x,ud.y,zeros(size(ud.x)));
set(triHndl,'EdgeColor',[1 .5 .5],'FaceColor','none');
hold off
set(gca,'XTick',[],'YTick',[]);
axis equal
axis([min(ud.x) max(ud.x) min(ud.y) max(ud.y)])

function LocalPlotFermat

k=1;
f=get(findobj(gcbf,'Tag','f'),'String');
if isequal(f,'?')
   f=2/(1+sqrt(5));
else
   f=eval(f);
end
d=f*2*pi;
minCount=1;
maxCount=eval(get(findobj(gcbf,'Tag','npts'),'String'));
theta1=((minCount:maxCount)*d)';
r1=k*sqrt(theta1);
ud.x=r1.*cos(theta1);
ud.y=r1.*sin(theta1);
set(gcf,'UserData',ud);   

theta2=(minCount:0.05:maxCount)*d;
r2=k*sqrt(theta2);

% Plot the points
ud=get(gcf,'UserData');

% Plot all the gridlines as a single line
[vx,vy]=voronoi(ud.x,ud.y);
vx=[vx; NaN*ones(1,size(vx,2))];
vy=[vy; NaN*ones(1,size(vy,2))];
cla
ud.xyHndl=plot(ud.x,ud.y,'Marker','.','MarkerSize',24, ...
  	'LineStyle','none');
ud.vHndl=line(vx(:),vy(:),'Color',[.9 .9 .9]);
line(r2.*cos(theta2),r2.*sin(theta2), ...
   'LineStyle','-', ...
   'Color',[1 .5 .5])
set(gca,'XTick',[],'YTick',[]);
axis equal
axis([min(ud.x) max(ud.x) min(ud.y) max(ud.y)])

