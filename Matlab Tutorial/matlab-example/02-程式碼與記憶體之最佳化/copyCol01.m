n = 1000000;

a = rand(3,1);
b=zeros(3,n);

tic
b1=a(:, ones(1,n));
time1=toc;

tic
b2=a*ones(1,n);
time2=toc;

fprintf('time1=%f, time2=%f\n', time1, time2);
