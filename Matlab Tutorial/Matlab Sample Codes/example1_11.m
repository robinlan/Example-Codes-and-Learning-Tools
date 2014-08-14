clear
fid = fopen('F:\MATLAB6.5\work\訊號處理─MATLAB 的應用(範例程式)\signal_1_11.txt');
x = fscanf(fid,'%7d%8d%8d%8d%8d%8d%8d%8d%8d%8d\n',50000);
fclose(fid);
plot(x,'r');
grid on;
xlabel('Sample No.')
ylabel('Amplitude')
title('Seismic Signal ANMBHZ89')
