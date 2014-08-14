[t, y]= ode15s('vdp2', [0 3000], [2 1]');
subplot(1,1,1);
plot(y(:, 1), y(:, 2), '-o');
xlabel('y(t)'); ylabel('y''(t)')