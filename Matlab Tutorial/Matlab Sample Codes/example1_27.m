clear
b_FIR = [1 -0.9 0.64 -0.576];
k_FIR = tf2latc(b_FIR)

b_IIR = [1 3 3 1];
a_IIR = [1 -0.9 0.64 -0.576];
[k_IIR,v_IIR] = tf2latc(b_IIR, a_IIR)
[num, den] = latc2tf(k_IIR)
