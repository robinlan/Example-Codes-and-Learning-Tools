t = 0:0.1:4*pi;
y = exp(-t/5).*sin(t);
h = plot(t, y);			% h 為曲線的握把
set(h, 'Linewidth', 3);		% 將曲線寬度改為 3
set(h, 'Marker', 'o');		% 將曲線的線標改成小圓圈
set(h, 'MarkerSize', 20);	% 將線標的大小改成 20