A = [1 3 4; 2 4 1; 1 6 2];
p = poly(A);		% 方陣的特徵多項式
r = roots(p);		% 特徵方程式的根，亦即固有值
det(A-r(1)*eye(3))
det(A-r(2)*eye(3))
det(A-r(3)*eye(3))