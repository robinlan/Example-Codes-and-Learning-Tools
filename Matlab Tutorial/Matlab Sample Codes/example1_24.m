clear
[b,a] = butter(10,200/1000);
gd = grpdelay(b,a,128);
[h,f] = freqz(b,a,128,2000);
pd = -unwrap(angle(h))*(2000/(2*pi))./f;
plot(f,gd,'-',f,pd,'--')
axis([0 1000 -30 30])
legend('Group Delay','Phase Delay')