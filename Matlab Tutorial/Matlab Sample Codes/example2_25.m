clear
n = 20;                             % Filter order
f = [0 0.4 0.5 1];                  % Frequency band edges
a = [1 1 0 0];                      % Desired amplitudes
w = [1 10];                         % Weight vector
b_II = remez(n,f,a);
b_II_W = remez(n,f,a,w);
fvtool(b_II_W,1,b_II,1)

%b_I  = firls(n,f,a);
%b_I_W  = firls(n,f,a,w);
%fvtool(b_I,1,b_I_W,1,b_II,1,b_II_W,1)

%[h_I, w_I ] = freqz(b_I ,1,n);
%[h_II,w_II] = freqz(b_II,1,n);

%plot( f , a.^2 , w_I/pi , abs(h_I).^2 , w_II/pi , abs(h_II).^2 )
%legend('Desired','firls','remez')
%xlabel('Normalized Frequency (\times\pi rad/sample)')
%ylabel('Magnitude Squared')
%title('Magnitude Squared Response')
%grid on