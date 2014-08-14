clear
F = [0 0.3 0.4 0.6 0.7 0.9];
A = [0 1.0 0.0 0.0 0.5 0.5];

for i = 1:2:6
    plot([F(i) F(i+1)],[A(i) A(i+1)],'-')
    hold on
end

FF = [0 0.3 0.4 0.6 0.7 0.9 1.0];
AA = [0 0.0 0.0 0.0 0.0 0.0 0.0];

for i = 1:2:6
    plot([FF(i+1) FF(i+2)],[AA(i+1) AA(i+2)],'r:')
    hold on
end

axis([0 1.0 0 1.0])
xlabel('Normalized frequency (f)')
ylabel('Desired amplitude response (a)')
text(0.5,0.9,'f = [0 .3 .4 .6 .7 .9]')
text(0.5,0.8,'a = [0 1 0 0 .5 .5]')
