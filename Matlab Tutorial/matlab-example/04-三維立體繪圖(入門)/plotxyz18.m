Z = peaks(50);
C(:, :, 1) = rand(50);	% C(:,:,1) 代表 R（Red，紅色）的份量
C(:, :, 2) = rand(50);	% C(:,:,2) 代表 G（Green，綠色）的份量
C(:, :, 3) = rand(50);	% C(:,:,3) 代表 B（Blue，藍色）的份量
surf(Z, C);
axis tight