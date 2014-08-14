clear
t = (0:1/999:1);
x = sin(2*pi*25*t);
y = dct( x)% Compute DCT
y2 = find(abs(y) < 0.9); % Use 17 coefficients
y(y2) = zeros(size(y2)); % Zero out points < 0.9
z = idct(y); % Reconstruct signal using inverse DCT
subplot(211); plot(t,x);
title('Original Signal')
subplot(212); plot(t,z), axis([0 1 -1 1])
title('Reconstructed Signal')
accuracy_reconstruction = norm(x-z)/norm(x)
