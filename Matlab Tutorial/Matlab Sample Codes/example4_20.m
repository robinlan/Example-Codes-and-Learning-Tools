clear
x1 = 1:10                              % 原始訊號
[xhat,nd] = cceps(x1)
x2 = icceps(xhat,nd)
