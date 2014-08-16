% This demo program was developed Hsiao-Lung Chan, Ph.D. September 28, 2009
% Spectral analysis

clear  % clear all variables

% Generating two sinusoids and one DC component
fs=2000;   % sampling at 2 kHz
t=0:1/fs:0.1;
%x=10 + 14*cos(2*pi*100*t-pi/3) + 4*cos(2*pi*250*t-pi/2);
%x=10+7*exp(j*(2*pi*100*t-pi/3))+2*exp(j*(2*pi*250*t-pi/2));
x=10+7*exp(j*(2*pi*100*t-pi/3))+7*exp(j*(-2*pi*100*t+pi/3))+2*exp(j*(2*pi*250*t-pi/2))+2*exp(j*(2*pi*250*t-pi/2));
subplot(2,2,1)
plot(t,x)
ylabel('x(n)')
xlabel('Time, s')
title('Two sinusoids + DC')
axis([min(t) max(t) min(x)*1.1 max(x)*1.1])

% Spectral analysis
Xf=fft(x);
resolution=fs/length(Xf);
f=(0:length(Xf)-1)*resolution;
Xf_mag = abs(Xf);  % magnitude of spectrum
subplot(2,2,2)
plot(f,Xf_mag)
xlabel('Frequency, Hz')
ylabel('Magnitude')
title('Spectrum')
axis([min(f) max(f) 0 max(Xf_mag)*1.1])

Xf_phase = angle(Xf);    % phase of spectrum
subplot(4,2,6)
plot(f,Xf_phase)
xlabel('Phase, rad')
ylabel('Phase')
axis([min(f) max(f) min(Xf_phase)*1.1 max(Xf_phase)*1.1])

Xf_phase = unwrap(Xf_phase);    % Unwrap phase angle
subplot(4,2,8)
plot(f,Xf_phase)
xlabel('Phase, rad')
ylabel('Unwrap phase')
axis([min(f) max(f) min(Xf_phase)*1.1 max(Xf_phase)*1.1])

% Shift zero-frequency component to center of spectrum
Xf_center=fftshift(Xf);
Xf_mag = abs(Xf_center);  % magnitude of spectrum
Xf_phase = angle(Xf_center);    % phase of spectrum
subplot(2,2,3)
f=f-fs/2;
plot(f,Xf_mag)
xlabel('Frequency, Hz')
ylabel('Magnitude')
title('Spectrum')
axis([min(f) max(f) 0 max(Xf_mag)*1.1])

