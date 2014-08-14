fileName='welcome.wav';
[y, fs, nbits]=wavread(fileName);
fprintf('音訊檔案 "%s" 的資訊：\n', fileName);
fprintf('音訊長度 = %g 秒\n', length(y)/fs);
fprintf('取樣頻率 = %g 取樣點/秒\n', fs);
fprintf('解析度 = %g 位元/取樣點\n', nbits);