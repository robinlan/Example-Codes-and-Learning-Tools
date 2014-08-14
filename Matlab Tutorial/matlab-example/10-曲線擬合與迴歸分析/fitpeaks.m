point_n = 10;
[xx, yy, zz] = peaks(point_n);
zz = zz + randn(size(zz));
subplot(2,2,1);
surf(xx, yy, zz);
axis tight

x = xx(:);
y = yy(:);
z = zz(:);

% Use Gaussians as the basis functions
%A = [];
%sigma = 1;
%gaussian_n = 50;
%center = rand(gaussian_n, 2)*6-3;
%for i = 1:gaussian_n,
%	A = [A exp(-sum(([x y]-ones(point_n^2,1)*center(i,:)).^2, 2)/sigma)];
%end

% Use the correct basis functions
A = [(1-x).^2.*exp(-(x.^2)-(y+1).^2), (x/5-x.^3-y.^5).*exp(-x.^2-y.^2), exp(-(x+1).^2-y.^2)]; 
theta = A\z

rmse = norm((A*theta-z))/sqrt(point_n)
z2 = A*theta;
zz2 = reshape(z2, point_n, point_n);
subplot(2,2,2);
surf(xx, yy, zz2);
axis tight;

point_n = 31;
[xx, yy] = meshgrid(linspace(-3,3,point_n), linspace(-3,3,point_n));
x = xx(:);
y = yy(:);
A = [(1-x).^2.*exp(-(x.^2)-(y+1).^2), (x/5-x.^3-y.^5).*exp(-x.^2-y.^2), exp(-(x+1).^2-y.^2)]; 
zz = reshape(A*theta, point_n, point_n);
subplot(2,2,3);
surf(xx, yy, zz);
axis tight;

point_n = 31;
[xx, yy, zz] = peaks(point_n);
subplot(2,2,4);
surf(xx, yy, zz);
axis tight;


% Plot the Gaussians
%AA = reshape(max(A, [], 2), point_n, point_n);
%subplot(2,2,3);
%surf(xx, yy, AA);
%axis tight;
