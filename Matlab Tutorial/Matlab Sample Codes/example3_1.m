clear
x = [1 1 1 1 1]';
y = x;
xyc_xcorr = xcorr(x,y)

y = x(length(x):-1:1);
xyc_conv = conv(x,y)