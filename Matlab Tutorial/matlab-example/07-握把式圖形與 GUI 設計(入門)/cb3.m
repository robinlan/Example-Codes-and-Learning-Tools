% 根據第三個UI控制物件來決定使用的色盤矩陣。
switch get(h3, 'value')
	case 1
		colormap(hsv);
	case 2
		colormap(hot);
	case 3
		colormap(cool);
	otherwise
		disp('Unknown option');
end
