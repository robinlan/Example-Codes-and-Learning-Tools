function poly = polynom(vec)
%POLYNOM Polynomial class constructor
%	poly = POLYNOM(vec) creates a polynomial object from the given vector vec
%	which contains the coefficients of the descending-order polynomial.

if isa(vec, 'polynom')		% 若 vec 已經是 polynom 物件，則直接設定成輸出
	poly = vec;
else
	poly.c = vec(:).';		% 將向量設定成 poly 的係數
	poly = class(poly, 'polynom');	% 將 poly 加持成 polynom 物件
end