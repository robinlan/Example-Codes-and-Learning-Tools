[t, y] = ode45('vdp1', [0 25], [3 3]');
plot(y(:,1), y(:,2), '-o');
xlabel('y(t)'); ylabel('y''(t)');