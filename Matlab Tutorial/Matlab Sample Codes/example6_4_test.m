clear

% To create a simple sinusoidal signal: 
fs = 22050;                    % sampling rate
T = 1/fs;                      % sampling period
t = [0:T:0.25];                % time vector
f1 = 50;                       % frequency #1
omega1 = 2*pi*f1;              % angular frequency #1
phi = 2*pi*0.75;               % arbitrary phase offset = 3/4 cycle
x1 = cos(omega1*t + phi);      % sinusoidal signal, amplitude = 1
figure(1)
plot(t,x1);
xlabel('Time (seconds)');
ylabel('x1');

% To calculate and view the FFT of the simple sinusoid: 
X1 = fft(x1);                  % FFT of x1
N = length(t);                 % N = length of fft
f = [0:N-1]*fs/(N-1);          % frequency vector
figure(2)
plot(f,abs(X1));               % plot magnitude
xlabel('Frequency (Hertz)');
ylabel('X1');
axis([0,fs/2,0,max(abs(X1))]); % display only frequencies < fs/2

% To create a more complex signal (using sinusoids): 
x1 = cos(omega1*t + phi);      % sinusoidal signal, amplitude = 1
x2 = cos(2*pi*150*t + phi)/3;  % sinusoidal signal, amplitude = 1/3
x3 = cos(2*pi*250*t + phi)/5;  % sinusoidal signal, amplitude = 1/5
x4 = cos(2*pi*350*t + phi)/7;  % sinusoidal signal, amplitude = 1/7
x5 = cos(2*pi*450*t + phi)/9;  % sinusoidal signal, amplitude = 1/9

xcomplex = x1+x2+x3+x4+x5;
figure(3)
plot(t,xcomplex);
xlabel('Time (seconds)');
ylabel('xcomplex');

% To calculate and view the FFT of the complex signal: 
Xcomplex = fft(xcomplex);      % FFT of xcomplex
figure(4)
plot(f,abs(Xcomplex));
xlabel('Frequency (Hertz)');
ylabel('Xcomplex');
axis([0,1000,0,max(abs(Xcomplex))]);

% Window xcomplex before doing FFT: 
Xcompwin = fft(xcomplex.*hanning(N).');
figure(5)
plot(f,abs(Xcompwin));
xlabel('Frequency (Hertz)');
ylabel('Windowed Xcomplex');
axis([0,1000,0,max(abs(Xcompwin))]);


% Zero-pad xcomplex before doing FFT: 
Xcompzp = fft([xcomplex,zeros(1,N)]);
fzp = [0:2*N-1]*fs/(2*N-1);
figure(6)
plot(fzp,abs(Xcompzp));
xlabel('Frequency (Hertz)');
ylabel('Zero-Padded Xcomplex');
axis([0,1000,0,max(abs(Xcompzp))]);