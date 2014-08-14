clear
[b,a] = butter(5,30/50);
Hd = dfilt.df2t(b,a);                  % Direct form II transposed structure

freqz(Hd)

t = (0:1/100:10-1/100);                % Time vector
x = sin(2*pi*15*t) + sin(2*pi*40*t);   % Signal
y = filter(Hd,x);
