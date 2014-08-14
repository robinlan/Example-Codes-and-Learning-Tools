caseNum=100;
A=zeros(caseNum, 4);	% A in Ax=b
b=zeros(caseNum, 1);	% b in Ax=b
for i=1:caseNum
	m=max([round(100*rand), 5]);
	n=max([round(100*rand), 5]);
	s=sprand(m, n, 0.1);
	sizeInfo=whos('s');
	A(i,1)=nnz(s);
	A(i,2:3)=sizeInfo.size;
	A(i,4)=1;
	b(i)=sizeInfo.bytes;
end
x=A\b
maxDiff=max(abs(A*x-b))