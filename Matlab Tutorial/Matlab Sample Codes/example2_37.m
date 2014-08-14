clear
[b,a] = butter(5,0.4);
Hd = dfilt.df2t(b,a);         % Implement direct form II transposed
x = [1:0.2:4]';               % data in x
y_1 = filter(Hd,x)

y_2 = filter(b,a,x)
