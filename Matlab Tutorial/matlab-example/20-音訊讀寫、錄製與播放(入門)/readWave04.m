fileName='flanger.wav';
[y, fs]=wavread(fileName);	% 讀取音訊檔
sound(y, fs);	% 播放音訊
left=y(:,1);	% 左聲道音訊
right=y(:,2);	% 右聲道音訊
subplot(2,1,1), plot((1:length(left))/fs, left);
subplot(2,1,2), plot((1:length(right))/fs, right);