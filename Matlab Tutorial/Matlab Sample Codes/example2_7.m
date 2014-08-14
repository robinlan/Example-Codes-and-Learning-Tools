clear
[b_1,a_1] = maxflat(3,3,0.25)
fvtool(b_1,a_1)

[b_2,a_2] = butter(3,0.25)
fvtool(b_2,a_2)

[b_3,a_3] = maxflat(3,1,0.25)
fvtool(b_3,a_3)

h = maxflat(4,'sym',0.3)
fvtool(h)
