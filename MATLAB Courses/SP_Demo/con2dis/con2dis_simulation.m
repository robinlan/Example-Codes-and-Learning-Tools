clear  % clear all variables

% specification of sinusoid
f0=8;
a=5;
phi=0;

% Using 200-Hz sampling rate
fs=200;
t=0:1/fs:0.5;
x=a*cos(2*pi*f0*t+phi);
subplot(3,2,1)
stem(t,x)
ylabel('x(n)')
title('fs = 200 Hz')
axis([min(t) max(t) -6 6])

% Recosntruct signal using a high sampling rate
fs_r=2000;
new_t=min(t):1/fs_r:max(t);
x_r=zeros(1,length(new_t));
for k=1:length(t)
  x_r=x_r+x(k)*sinc((new_t-t(k))*fs); 
end
subplot(3,2,2)
plot(new_t,x_r)
ylabel('xr')
title('Reconstructed signal')
axis([min(new_t) max(new_t) -6 6])


% Using 20-Hz sampling rate
fs=20;
t=0:1/fs:0.5;
x=a*cos(2*pi*f0*t+phi);
subplot(3,2,3)
stem(t,x)
ylabel('x(n)')
title('fs = 20 Hz')
axis([min(t) max(t) -6 6])

% Recosntruct signal using a high sampling rate
fs_r=2000;
new_t=min(t):1/fs_r:max(t);
x_r=zeros(1,length(new_t));
for k=1:length(t)
  x_r=x_r+x(k)*sinc((new_t-t(k))*fs); 
end
subplot(3,2,4)
plot(new_t,x_r)
ylabel('xr')
title('Reconstructed signal')
axis([min(new_t) max(new_t) -6 6])


% Using 10-Hz sampling rate
fs=10;
t=0:1/fs:0.5;
x=a*cos(2*pi*f0*t+phi);
subplot(3,2,5)
stem(t,x)
xlabel('Time, sec')
ylabel('x(n)')
title('fs = 10 Hz')
axis([min(t) max(t) -6 6])

% Recosntruct signal using a high sampling rate
fs_r=2000;
new_t=min(t):1/fs_r:max(t);
x_r=zeros(1,length(new_t));
for k=1:length(t)
  x_r=x_r+x(k)*sinc((new_t-t(k))*fs); 
end
subplot(3,2,6)
plot(new_t,x_r)
xlabel('Time, sec')
ylabel('xr')
title('Reconstructed signal')
axis([min(new_t) max(new_t) -6 6])
