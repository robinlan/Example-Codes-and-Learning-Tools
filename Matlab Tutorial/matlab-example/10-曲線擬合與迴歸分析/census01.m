load census.mat			% 更戈
plot(cdate, pop, 'o');		% cdate pop 羆计
A = [ones(size(cdate)), cdate, cdate.^2];
y = pop;
theta = A\y;			% ノオ埃т程ㄎ theta 
plot(cdate, pop, 'o', cdate, A*theta, '-');
legend('龟悔计', '箇代计');
xlabel('');
ylabel('瓣羆计');