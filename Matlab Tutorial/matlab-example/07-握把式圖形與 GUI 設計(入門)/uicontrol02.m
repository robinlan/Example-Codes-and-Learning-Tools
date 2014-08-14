close all		% 關閉所有圖形視窗
uicontrol('style', 'push', 'position',  [200  20 80 30]);  
uicontrol('style', 'slide', 'position', [200  70 80 30]);  
uicontrol('style', 'radio', 'position', [200 120 80 30]);  
uicontrol('style', 'frame', 'position', [200 170 80 30]);  
uicontrol('style', 'check', 'position', [200 220 80 30]);  
uicontrol('style', 'edit', 'position', [200 270 80 30]);  
uicontrol('style', 'list', 'position', [200 320 80 30], 'string', '1|2|3|4');  
uicontrol('style', 'popup', 'position', [200 370 80 30], 'string', 'one|two|three');
