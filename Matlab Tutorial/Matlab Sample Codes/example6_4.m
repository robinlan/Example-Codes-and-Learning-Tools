clear

% To create a complex signal: 
fs = 1;                        % sampling rate
T = 1/fs;                      % sampling period
t = [0:T:20];                  % time vector
alpha = -0.1+j*0.3;
x = exp(alpha*t);
x_real = real(x);
x_imag = imag(x);
x_abs = abs(x);
x_angle = angle(x);

subplot(221)
plot(t,x_real);
xlabel('t');
ylabel('Re[x] = e^\sigmacos\omegat');
title('Real part of x(t) = e^\alpha^t');

subplot(222)
plot(t,x_imag);
xlabel('t');
ylabel('Im[x] = e^\sigmasin\omegat');
title('Imaginary part of x(t) = e^\alpha^t');

subplot(223)
plot(t,x_abs);
xlabel('t');
ylabel('|x| = e^\sigma');
title('Magnitude of x(t) = e^\alpha^t');

subplot(224)
plot(t,x_angle);
xlabel('t');
ylabel('arg x = \omegat');
title('Phase angle of x(t) = e^\alpha^t');
