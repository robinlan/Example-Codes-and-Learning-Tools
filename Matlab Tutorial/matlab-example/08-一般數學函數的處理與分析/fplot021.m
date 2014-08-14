subplot(2,1,1);
fplot('sin(2*x)+cos(x)', [-10, 10])		% 使用字串指定函式
subplot(2,1,2);
fplot(@(x)sin(2*x)+cos(x), [-10, 10])		% 使用函式握把來指定函式