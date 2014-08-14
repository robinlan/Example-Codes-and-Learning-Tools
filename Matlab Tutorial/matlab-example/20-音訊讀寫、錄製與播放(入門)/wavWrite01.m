fs=11025;		% 取樣頻率
duration=2;		% 錄音時間
waveFile='test.wav';	% 欲儲存的 wav 檔案
fprintf('按任意鍵後開始 %g 秒錄音：', duration); pause
fprintf('錄音中...');
y=wavrecord(duration*fs, fs);
fprintf('錄音結束\n');
fprintf('按任意鍵後開始儲存音訊至 %s 檔案...', waveFile); pause
nbits=8;			% 每點的解析度為 8-bit
wavwrite(y, fs, nbits, waveFile);
fprintf('存檔結束\n');
fprintf('按任意鍵後開始播放 %s...\n', waveFile);
dos(['start ', waveFile]);	% 開啟與 wav 檔案對應的應用程式