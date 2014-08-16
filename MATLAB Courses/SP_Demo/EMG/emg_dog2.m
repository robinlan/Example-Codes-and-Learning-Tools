% This program and data is partially modified or extracted from WWW of R.
% Rangayyan, 2002 and written by Hsiao-Lung chan Ph.D, 2011

clear all               % clears all active variables
close all

emg = load('emg_dog2.dat');
fs = 10000;         % sampling rate for emg
emg = emg*1000/20;   % gain = 20,000; emg now in micro volts

slen = length(emg);
t=(1:slen)/fs; 

figure(1)
clf
set(gcf,'color','w')  % set white background
subplot(3,1,1)
plot(t, emg)
axis([min(t) max(t) min(emg)*1.1 max(emg)*1.1])
xlabel('Time, s');
ylabel('EMG in microvolts');

% Envelope of EMG obtained using a full-wave rectifier and a lowpass filter
env = load('emg_dog2_env.dat');
fs1 = 1000;     % sampling rate for envelope and air flow channels
slen1 = length(env);
t1=(1:slen1)/fs1;   

% inspiratory airflow measured with a pneumo-tachograph, in liters/second
flow = load('emg_dog2_flo.dat');

subplot(3,1,2)
plot(t1, env);
ylabel('Filtered EMG envelope');
xlabel('Time, s');
axis([min(t1) max(t1) min(env) max(env)+(max(env)-min(env))/10])

subplot(3,1,3)
plot(t1, flow);
ylabel('Air flow, liters/sec');
xlabel('Time, s');
axis([min(t1) max(t1) min(flow) max(flow)*1.1])


% ======== display airflow vs. EMG envelop =========
figure(2)
clf
set(gcf,'color','w')  % set white background
plot(flow,env,'.')
xlabel('Airflow in liters per second')
ylabel('EMG envelop')