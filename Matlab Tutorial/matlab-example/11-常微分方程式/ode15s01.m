[t, y]= ode15s('vdp2', [0 3000], [2 1]');
subplot(2,1,1); plot(t, y(:,1), '-o');
xlabel('Time t'); ylabel('y(t)');
subplot(2,1,2); plot(t, y(:,2), '-o');
xlabel('Time t'); ylabel('y''(t)');	% 注意單引號「'」的重覆使用 