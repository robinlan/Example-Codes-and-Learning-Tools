function eigValue = myEigValue(mat)
%myEigValue: Generate the eigenvalues of the given matrix

%	Roger Jang, 20080210

if (isstr(mat))			% 若輸入是字串，轉成數值
	mat=eval(mat);
end
[a, b] = eig(mat);
eigValue=diag(b);
disp(eigValue);			% 顯示固有值 eigenValue