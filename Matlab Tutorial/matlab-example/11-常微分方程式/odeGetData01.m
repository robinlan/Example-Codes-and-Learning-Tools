[t, y] = ode45('vdp1', [0 25], [3 3]');
plot(t, y(:,1), t, y(:,2));
xlabel('Time t'); ylabel('Solution y(t) and y''(t)');
legend('y(t)', 'y''(t)');