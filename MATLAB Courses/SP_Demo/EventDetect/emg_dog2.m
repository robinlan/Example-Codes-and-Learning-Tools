% This program and data is modified or extracted from WWW of R. Rangayyan, 2002

clear all               % clears all active variables
close all

emg = load('emg_dog2.dat');
fs1 = 10000;         % sampling rate for emg
emg = emg*1000/20;   % gain = 20,000; emg now in micro volts

slen1 = length(emg);
t1=[1:slen1]/fs1; 

clf
subplot(3,1,1)
plot(t1, emg)
axis([min(t1) max(t1) min(emg)*1.1 max(emg)*1.1])
xlabel('Time, s');
ylabel('EMG in microvolts');

% Envelope of EMG obtained using a full-wave rectifier and a lowpass filter
env = load('emg_dog2_env.dat');
fs2 = 1000;     % sampling rate for envelope and air flow channels
slen2 = length(env);
t2=[1:slen2]/fs2;   

% inspiratory airflow measured with a pneumo-tachograph, in liters/second
flow = load('emg_dog2_flo.dat');

subplot(3,1,2)
plot(t2, env);
ylabel('Filtered EMG envelope');
xlabel('Time, s');
axis([min(t1) max(t1) min(env) max(env)+(max(env)-min(env))/10])

subplot(3,1,3)
plot(t2, flow);
ylabel('Air flow, liters/sec');
xlabel('Time, s');
axis([min(t1) max(t1) min(flow) max(flow)*1.1])