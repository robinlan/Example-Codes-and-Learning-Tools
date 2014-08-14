clear
[z,p,k] = cheb1ap(5,3);
[A,B,C,D] = zp2ss(z,p,k);                  % Convert to state-space form.

fs = 2;                                    % Sampling frequency (hertz)
u1 = 2*fs*tan(0.1*(2*pi/fs)/2);            % Lower band edge (rad/s)
u2 = 2*fs*tan(0.5*(2*pi/fs)/2);            % Upper band edge (rad/s)
Bw = u2 - u1;                              % Bandwidth
Wo = sqrt(u1*u2);                          % Center frequency
[At,Bt,Ct,Dt] = lp2bp(A,B,C,D,Wo,Bw);

[Ad,Bd,Cd,Dd] = bilinear(At,Bt,Ct,Dt,fs);

[bz,az] = ss2tf(Ad,Bd,Cd,Dd);               % convert to TF form.
fvtool(bz,az)
