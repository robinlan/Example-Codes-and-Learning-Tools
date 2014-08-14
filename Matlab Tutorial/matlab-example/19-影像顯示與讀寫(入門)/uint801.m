load clown.mat
Z8 = uint8(X-1);	% 將 X-1 轉成 uint8 的資料型態  
close all           % 關掉所有的圖形視窗  
image(Z8);
colormap(map);
colorbar;