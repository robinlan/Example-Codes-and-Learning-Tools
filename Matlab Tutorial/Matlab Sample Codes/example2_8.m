clear
b_1 = 0.4*sinc(0.4*(-25:25));
fvtool(b_1,1)

b_2 = 0.4*sinc(0.4*(-25:25));
b_3 = b_2.*hamming(51)';
fvtool(b_1,1,b_3,1)
