clear
t = (0:0.001:1)';
y1 = [1; zeros(99,1)]; % impulse
y2 = ones(100,1);      % step (filter assumes 0 initial cond.)
y3 = t;                % ramp

subplot(221)
plot(t(1:61),y1(1:61),'linewidth',2)
xlabel('Time')
ylabel('Amplitude')
title('Unit Impulse Function')

subplot(222)
plot(t(1:61),y2(1:61),'linewidth',2)
xlabel('Time')
ylabel('Amplitude')
title('Unit Step Function')

subplot(223)
plot(t(1:61),y3(1:61),'linewidth',2)
xlabel('Time')
ylabel('Amplitude')
title('Unit Ramp Function')