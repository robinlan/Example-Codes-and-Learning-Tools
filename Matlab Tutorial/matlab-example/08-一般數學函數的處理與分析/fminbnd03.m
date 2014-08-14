opt = optimset( 'disp', 'iter', 'TolX', 0.1);		% 顯示每個步驟的結果並設定誤差容忍值
[x, minValue] = fminbnd(@humps, 0.3, 1, opt)