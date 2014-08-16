clear
emg1=load('emg_dog2.dat');
env=load('emg_dog2_env.dat');
flow=load('emg_dog2_flo.dat');
set(gcf,'color','w')

subplot(3,2,1);

plot(emg1)

subplot(3,2,3);
env1=env(1:5750);
plot(env1)

subplot(3,2,5);
flow1=flow(1:5750);
plot(flow1)

subplot(2,2,2);
plot(flow1,env1)

%  index=1047:1680;
%  subplot(2,2,2)
%  plot(flow(index),env(index))
 
 subplot(2,2,4)
 index=1680:2054;
 plot(flow(index),env(index))
 set(gcf,'color','w')
 
