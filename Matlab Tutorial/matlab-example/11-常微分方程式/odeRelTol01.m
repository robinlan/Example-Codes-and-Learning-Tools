subplot(2,1,1);
ode45('vdp1', [0 25], [3 3]');
title('RelTol=0.01');
options = odeset('RelTol', 0.00001);
subplot(2,1,2);
ode45('vdp1', [0 25], [3 3]', options);
title('RelTol=0.0001');