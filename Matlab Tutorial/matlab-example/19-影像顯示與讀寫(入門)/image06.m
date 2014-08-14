load mandrill.mat
[m, n] = size(X);
figure ('unit', 'pixel', 'position', [200, 200, n, m]);
image(X);
colormap(map);
set(gca, 'position', [0, 0, 1, 1]);
