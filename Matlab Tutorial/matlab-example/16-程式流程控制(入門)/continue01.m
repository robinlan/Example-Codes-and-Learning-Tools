x = [1 -2 3 -4 5];
posTotal = 0;
for i = 1:length(x)
	if x(i)<0, continue; end	% 若 x(i) 小於零，直接跳到此迴圈的下一個輪迴
	posTotal=posTotal+x(i);
end
posTotal	% 顯示 posTotal 的值