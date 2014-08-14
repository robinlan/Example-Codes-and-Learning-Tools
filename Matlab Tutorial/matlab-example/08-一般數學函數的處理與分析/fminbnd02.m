opt = optimset('disp', 'iter');		% 顯示每個步驟的結果
[x, minValue] = fminbnd(@humps, 0.3, 1, opt)