[y, fs]=wavread('welcome.wav');
wavplay(y, 1.0*fs, 'sync');	% 播放 1.0 倍速度的音訊
wavplay(y, 0.9*fs, 'sync');	% 播放 0.9 倍速度的音訊
wavplay(y, 0.8*fs, 'sync');	% 播放 0.8 倍速度的音訊
wavplay(y, 0.6*fs, 'sync');	% 播放 0.6 倍速度的音訊