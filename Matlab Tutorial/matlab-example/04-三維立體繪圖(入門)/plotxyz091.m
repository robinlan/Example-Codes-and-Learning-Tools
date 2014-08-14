subplot(2,2,1);
ezmesh('sin(x)/x*sin(y)/y');
subplot(2,2,2);
ezsurf('sin(x*y)/(x*y)');
subplot(2,2,3);
ezmeshc('sin(x)/x*sin(y)/y');
subplot(2,2,4);
ezsurfc('sin(x*y)/(x*y)');