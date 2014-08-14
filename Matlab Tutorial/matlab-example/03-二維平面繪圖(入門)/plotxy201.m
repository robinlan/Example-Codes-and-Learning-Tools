n=100000;
bin=100;
subplot(211); hist( rand(n, 1), bin);
subplot(212); hist(randn(n, 1), bin);