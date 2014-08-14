x = 3:6;
y = 5:9;
zz=zeros(length(y), length(x));
for i=1:size(zz,1)
	for j=1:size(zz,2)
		zz(i,j)=y(i)*x(j);
	end
end
mesh(x, y, zz);
xlabel('X'); ylabel('Y'); zlabel('Z'); axis tight