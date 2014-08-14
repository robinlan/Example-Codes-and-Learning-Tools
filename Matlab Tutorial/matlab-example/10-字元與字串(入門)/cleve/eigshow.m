function eigshow(arg)
%EIGSHOW Graphical demonstration of matrix eigenvalues.
%   EIGSHOW presents a graphical experiment showing the effect of the
%   mapping induced on the unit circle by various 2-by-2 matrices.
%   Use the mouse to move the vector x and follow the resulting A*x.
%   When A*x is parallel to x, then x is an eigenvector of A and the
%   length of A*x is the eigenvalue.  The figure title is a menu of
%   selected matrices, including some with fewer than two real eigenvectors.
%   EIGSHOW(A) includes A, which must be 2-by-2, in the menu.
%
%   See also SVDSHOW.

%   Copyright (c) 1993-97 by The MathWorks, Inc.
%   $Revision: $  $Date: $

if nargin == 0;
   eigshowinit(1);
elseif isequal(arg,-1)
   eigshowaction
else
   eigshowinit(arg);
end

%------------------

function eigshowinit(arg)

mats = get(findobj(gcf,'type','uicontrol'),'userdata');
if isempty(mats)
   mats = {
      '[1 3; 4 2]/4'
      '[1 0; 0 1]'
      '[0 1; 1 0]'
      '[0 1; -1 0]'
      '[1 3; 4 2]/5'
      '[1 3; 2 4]/5'
      '[3 1; 4 2]/5'
      '[3 1; -2 4]/5'
      '[2 4; -1 -2]/4'
      '[6 4; -1 2]/4'
      'randn(2,2)'};
end

if all(size(arg)==1)
   if (arg < length(mats))
      mindex = arg;
      A = eval(mats{mindex});
   else
      A = randn(2,2);
      S = ['[' sprintf('%4.2f %4.2f; %4.2f %4.2f',A) ']'];
      mindex = length(mats);
      mats = [mats(1:mindex-1); {S}; mats(mindex)];
   end
else
   A = arg;
   if isstr(A)
      S = A;
      A = eval(A);
   else
      S = ['[' sprintf('%4.2f %4.2f; %4.2f %4.2f',A) ']'];
   end
   if any(size(A) ~= 2)
      error('Matrix must be 2-by-2')
   end
   mats = [{S};  mats];
   mindex = 1;
end

clf
uicontrol(...
   'style','popup', ...
   'units','normalized', ...
   'position',[.28 .92 .48 .08], ...
   'string',mats, ...
   'fontname','courier', ...
   'fontweight','bold', ...
   'fontsize',14, ...
   'value',mindex, ...
   'userdata',mats, ...
   'callback','eigshow(get(gco,''value''))');

s = 1.1*max(1,norm(A));
axis([-s s -s s])
axis square
xlabel('Make A*x parallel to x','fontweight','bold')
xcolor = [0 .6 0];
Axcolor = [0 0 .8];
h.x = eigshowvec([1 0]','x',xcolor);
h.Ax = eigshowvec(A(:,1),'Ax',Axcolor);
set(gcf,'userdata',A);
set(gca,'userdata',h);
set(gcf,'windowbuttondownfcn', ...
  'eigshow(-1); set(gcf,''windowbuttonmotionfcn'',''eigshow(-1)'')')
set(gcf,'windowbuttonupfcn', ...
  'set(gcf,''windowbuttonmotionfcn'','''')')

%------------------

function eigshowaction
A = get(gcf,'userdata');
h = get(gca,'userdata');
pt = get(gca,'currentpoint');
x = pt(1,1:2)';
x = x/norm(x);
eigshowact(x,h.x);
eigshowact(A*x,h.Ax);

%------------------

function h = eigshowvec(x,t,color)
h.mark = line(x(1),x(2),'marker','.','erase','none','color',color);
h.line = line([0 x(1)],[0 x(2)],'erase','xor','color',color);
h.text = text(x(1)/2,x(2)/2,t,'fontsize',12,'erase','xor','color',color);

%------------------

function eigshowact(x,h)
set(h.mark,'xdata',x(1),'ydata',x(2));
set(h.line,'xdata',[0 x(1)],'ydata',[0 x(2)]);
set(h.text,'pos',x/2);

