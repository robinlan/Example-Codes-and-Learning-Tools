function dy = lorenzOde(t, y)
% LORENZODE: ODE file for Lorenz chaotic equation
SIGMA = 10.;
RHO = 28;
BETA = 8./3.;
A = [ -BETA    0     y(2)
         0  -SIGMA   SIGMA
      -y(2)   RHO    -1  ];
dy = A*y;