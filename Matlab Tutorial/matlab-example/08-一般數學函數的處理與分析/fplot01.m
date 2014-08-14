subplot(2,1,1);
fplot('humps', [0,2]);		% 使用字串指定函式
subplot(2,1,2);
fplot(@humps, [0 2]);		% 使用函式握把來指定函式