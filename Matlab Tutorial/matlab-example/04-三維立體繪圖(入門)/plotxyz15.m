[X, Y, Z] = peaks;
surf(X, Y, Z, gradient(Z));
axis tight;
colormap hot