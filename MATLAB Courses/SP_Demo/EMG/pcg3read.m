% This program and data is modified or extracted from WWW of R. Rangayyan, 2002
% Read the combined PCG/ECG/carotid pulse datafile

close all
clear all

% Load the PCG, ECG, and carotid pulse data
sig = load('pec22.dat');
fs = 1000;

% Separating PCG, ECG, and carotid signals
pcg = sig(:,1);
ecg = sig(:,2);
carotid = sig(:,3);

% Plot all signals
t = (0 :length(pcg)-1)/fs;

clf
set(gcf,'color','w')
subplot(3,1,1);
plot(t, pcg);
ylabel('PCG');
title('PCG, ECG, and carotid pulse signals');
axis([min(t) max(t) min(pcg)*1.1 max(pcg)*1.1])

subplot(3,1,2);
plot(t, ecg);
ylabel('ECG');
axis([min(t) max(t) min(ecg)*1.1 max(ecg)*1.1])

subplot(3,1,3);
plot(t, carotid);
ylabel('Carotid pulse');
xlabel('Time, s');
axis([min(t) max(t) min(carotid)*1.1 max(carotid)*1.1])
