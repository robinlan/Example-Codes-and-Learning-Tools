[y, fs]=wavread('welcome.wav');
wavplay(y, 1.0*fs, 'sync');	% 播放 1.0 倍速度的音訊
wavplay(y, 1.2*fs, 'sync');	% 播放 1.2 倍速度的音訊
wavplay(y, 1.5*fs, 'sync');	% 播放 1.5 倍速度的音訊
wavplay(y, 2.0*fs, 'sync');	% 播放 2.0 倍速度的音訊