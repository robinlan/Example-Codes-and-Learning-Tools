[X Y Z] = sphere(25);
 sz = size(Z);
 C = rand(sz(1),sz(2),3);
 
% stitch data to wrap color around sphere
 C(:,end,:) = C(:,1,:);
 
% render an image
 figure('color',[0 0 0]);
 sp = surf(X,Y,Z,C,'FaceColor','interp',...
     'FaceLighting','phong');
 material shiny
 axis off vis3d
 cl = camlight('right');
 camzoom(1.5)
 set(sp,'EdgeColor','none');
 set(sp,'EdgeAlpha',1);
 drawnow
 
% setup the rotation vector for the movie
 view_vect_yaw = [-180:3:180];
 sz_vect = size(view_vect_yaw);
 
% loop through image frames
 for f = 1:sz_vect(2)
     % set the view, reset light position and render
     view([view_vect_yaw(f) 15]);
     camlight(cl,'right')
     drawnow
     % grab a frame
     m(f) = getframe(gcf);
 end
 
 % % format frames as mpeg and write to file
 % mpgwrite(m,C,'my_truecolor_movie.mpg',[1, 0, 1, 0, 10, 1, 1, 1]);
 
% ..or write to an avi file
% movie2avi(m,'my_movie.avi','compression','none','fps',25,'quality',100);
 
% end of code
 