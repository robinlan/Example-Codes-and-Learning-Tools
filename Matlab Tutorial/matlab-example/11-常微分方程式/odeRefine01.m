subplot(2,1,1);
ode23('vdp1', [0 25], [3 3]');
subplot(2,1,2);
options = odeset('Refine', 3);
ode23('vdp1', [0 25], [3 3]', options);