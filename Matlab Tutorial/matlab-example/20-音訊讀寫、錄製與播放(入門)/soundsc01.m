[y, fs]=wavread('welcome.wav');
sound(y, fs);
fprintf('Press any key to continue...\n'); pause
soundsc(y, fs);