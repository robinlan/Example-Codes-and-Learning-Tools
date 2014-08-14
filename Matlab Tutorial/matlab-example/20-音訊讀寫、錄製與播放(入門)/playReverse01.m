[y, fs]=wavread('welcome.wav');
wavplay(y, fs, 'sync');			% 播放正常的音訊波形
wavplay(-y, fs, 'sync');		% 播放上下顛倒的音訊波形
wavplay(flipud(y), fs, 'sync');		% 播放前後顛倒的音訊波形