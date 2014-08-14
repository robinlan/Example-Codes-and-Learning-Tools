subplot(2,1,1);
ode45('vdp4', [], [], [], 1);	% mu=1
subplot(2,1,2);
ode45('vdp4', [], [], [], 3);	% mu=3