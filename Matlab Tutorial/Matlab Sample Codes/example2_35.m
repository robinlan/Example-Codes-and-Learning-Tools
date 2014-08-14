clear
[z,p,k] = cheb1ap(5,3);

[A,B,C,D] = zp2ss(z,p,k);                   % Convert to state-space form.

u1 = 0.1*2*pi; u2 = 0.5*2*pi;               % In radians per second
Bw = u2-u1;
Wo = sqrt(u1*u2);
[At,Bt,Ct,Dt] = lp2bp(A,B,C,D,Wo,Bw);

[Ad,Bd,Cd,Dd] = bilinear(At,Bt,Ct,Dt,2,0.1);

[bz,az] = ss2tf(Ad,Bd,Cd,Dd);               % convert to TF form.
fvtool(bz,az)
