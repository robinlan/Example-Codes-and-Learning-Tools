x = 3:6;
y = 5:9;
[xx, yy] = meshgrid(x, y);		% xx 和 yy 都是矩陣  
zz = xx.*yy;				% 計算函數值 zz，也是矩陣
subplot(2,2,1); mesh(xx);
title('xx'); axis tight
subplot(2,2,2); mesh(yy);
title('yy'); axis tight
subplot(2,2,3); mesh(xx, yy, zz);
title('zz 對 xx 及 yy 作圖'); axis tight