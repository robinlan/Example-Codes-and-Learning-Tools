x = linspace(0,4*pi,300);
subplot(121)
%hold on
plot(x,diric(x,7),'linewidth',2)
xlabel('Time')
ylabel('Amplitude')
title('Dirichlet Function n = 7')
%hold off

subplot(122)
%hold on
plot(x,diric(x,8),'linewidth',2)
xlabel('Time')
ylabel('Amplitude')
title('Dirichlet Function n = 8')
%hold off
