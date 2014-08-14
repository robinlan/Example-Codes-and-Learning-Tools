clear
load mtlb
whos
Fs
[p,q] = rat(8192/Fs,0.0001)
y = resample(mtlb,p,q);

sound(mtlb,Fs)
sound(y,p/q*Fs)
t1 = 0:4000;            % Time vector
t2 = 0:length(y)-1;     % New time vector
plot(t1,mtlb,'*',t2,y,'o')
legend('original','resampled')
xlabel('Time')
