clear
[z,p,k] = cheb1ap(5,3);

[A,B,C,D] = zp2ss(z,p,k);                   % Convert to state-space form.

u1 = 0.1*2*pi; u2 = 0.5*2*pi;               % In radians per second
Bw = u2-u1;
Wo = sqrt(u1*u2);
[At,Bt,Ct,Dt] = lp2bp(A,B,C,D,Wo,Bw);

[b,a] = ss2tf(At,Bt,Ct,Dt);                 % Convert to TF form.

[bz,az] = impinvar(b,a,2);
fvtool(bz,az)
