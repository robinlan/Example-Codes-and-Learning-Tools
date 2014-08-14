clear
n = 20;                             % Filter order
f = [0.0 0.4 0.42 0.48 0.5 1.0];
a = [1.0 1.0 0.80 0.20 0.0 0.0];    % Passband,linear transition,stopband
b_I  = firls(n,f,a);
b_II = remez(n,f,a);

[h_I, w_I ] = freqz(b_I ,1,n);
[h_II,w_II] = freqz(b_II,1,n);

plot( f , a.^2 , w_I/pi , abs(h_I).^2 , w_II/pi , abs(h_II).^2 )
legend('Desired','firls','remez')
xlabel('Normalized Frequency (\times\pi rad/sample)')
ylabel('Magnitude Squared')
title('Magnitude Squared Response')
grid on
