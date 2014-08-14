fprintf('MATLAB version = %s\n', version);
ns = 10*(1:30);
for j=1:length(ns)
	n = ns(j);
	a = rand(n);
	b = rand(n);
	% 第一種方法：for-loop operation
	c1 = zeros(n);
	tic
	for p = 1:n
		for q = 1:n
			c1(p, q) = a(p, q)*b(p, q);
		end
	end
	time1(j)=toc;
	% 第二種方法：vectorized operation
	tic
	c2 = a.*b;
	time2(j)=toc;
end
subplot(2,1,1);
plot(ns, time1, 'v-', ns, time2, '^-'); grid on
legend('time1 for for-loop code', 'time2 for vectorized code', 'location', 'NorthWest');
xlabel('n'); ylabel('second');
subplot(2,1,2);
plot(ns, time1./time2, '.-'); grid on
xlabel('n'); ylabel('time1/time2');
fprintf('isequal(c1, c2) = %g\n', isequal(c1, c2));