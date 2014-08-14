clear

x = linspace(0,4*pi,300);
subplot(121)
plot(x,diric(x,7),'linewidth',2)
xlabel('Time')
ylabel('Amplitude')
title('Dirichlet Function n = 7')

subplot(122)
plot(x,diric(x,8),'linewidth',2)
xlabel('Time')
ylabel('Amplitude')
title('Dirichlet Function n = 8')