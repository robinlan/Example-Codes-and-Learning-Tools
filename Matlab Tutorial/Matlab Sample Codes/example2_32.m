clear
b_1 = cremez(62,[0 0.5 0.55 1],{'lowpass',-16});      
fvtool(b_1,1)

b_2 = cremez(29,[0 0.5 0.55 1],'lowpass'),
fvtool(b_2,1)