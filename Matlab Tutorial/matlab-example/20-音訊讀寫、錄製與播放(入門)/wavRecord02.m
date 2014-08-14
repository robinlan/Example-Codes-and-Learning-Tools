fs=11025;		% 取樣頻率
duration=2;		% 錄音時間
channel=1;		% 單聲道
fprintf('按任意鍵後開始 %g 秒錄音：', duration); pause
fprintf('錄音中...');
y=wavrecord(duration*fs, fs, channel, 'uint8');	% duration*fs 是錄音資料點數
fprintf('錄音結束\n');
fprintf('按任意鍵後開始播放：'); pause
wavplay(y,fs);