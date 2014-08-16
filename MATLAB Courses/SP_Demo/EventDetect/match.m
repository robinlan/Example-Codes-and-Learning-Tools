
load eeg_spike
x=eegs(3,:);
subplot(4,1,1);
plot(x);
template=x(62:85);

subplot(4,2,3);
plot(template);

subplot(4,2,4);
index=length(template):-1:1;
b=template(index);
plot(b);

 subplot(4,1,3);
% z=fft(x);
% z_im=abs(z);
% plot(z_im)
index2=length(x):-1:1;
c=x(index2);
plot(c)

subplot(4,1,4)
%b=Num;
a=1;
y=filter(b,a,c);
plot(y)


