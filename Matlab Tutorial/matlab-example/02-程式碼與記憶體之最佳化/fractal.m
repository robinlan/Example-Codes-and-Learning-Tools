clear all; close all

tic
xmax=2;
steps=401;
maxiter=32;

Z=0;
X=linspace(-xmax, xmax, steps);
Y=linspace(-xmax, xmax, steps);

for p=1:steps
	for q=1:steps
		c=-xmax+2*xmax*q/steps-0.5 + i*(-xmax+2*xmax*p/steps);;
		z=c;
		for r=0:maxiter
			z=z*z+c;
			if abs(z)>2; break; end
		end
		Z(p,q)=sqrt(r/maxiter);
	end
end
toc

subplot(2,2,1);
mesh(X,Y,Z)
subplot(2,2,2);
imagesc(Z); axis image