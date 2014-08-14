function mouse02(action)
% mouse02: 本例展示如何以來設定滑鼠事件的反應指令

%	Roger Jang, 20040406

if nargin == 0, action = 'start'; end

switch(action)	% 開啟圖形視窗
	case 'start'
		axis([0 1 0 1]);	% 設定圖軸範圍
		box on;			% 將圖軸加上圖框
		title('Click and drag your mouse in this window!');
		% 設定滑鼠按鈕被按下時的反應指令
		set(gcf, 'WindowButtonDownFcn', sprintf('%s %s', mfilename, 'down'));
	case 'down'	% 滑鼠按鈕被按下時的反應指令
		% 設定滑鼠移動時的反應指令
		set(gcf, 'WindowButtonMotionFcn', sprintf('%s %s', mfilename, 'move'));
		% 設定滑鼠按鈕被釋放時的反應指令為
		set(gcf, 'WindowButtonUpFcn', sprintf('%s %s', mfilename, 'up'));
		% 列印「Mouse down!」訊息
		fprintf('Mouse down!\n');
	case 'move'	% 滑鼠移動時的反應指令
		fprintf('Mouse is moving! ');
		feval(mfilename, 'print');
		% 列印「Mouse is moving!」訊息及滑鼠現在位置
	case 'up'	% 滑鼠按鈕被釋放時的反應指令
		feval(mfilename, 'print');
		% 清除滑鼠移動時的反應指令
		set(gcf, 'WindowButtonMotionFcn', '');
		% 清除滑鼠按鈕被釋放時的反應指令
		set(gcf, 'WindowButtonUpFcn', '');
		% 列印「Mouse up!」訊息
		fprintf('Mouse up!\n');
	case 'print'
		currPt = get(gca, 'CurrentPoint');
		x = currPt(1,1); y = currPt(1,2);
		line(x, y, 'marker', '.', 'EraseMode', 'xor');
		fprintf('Current location = (%g, %g)\n', currPt(1,1), currPt(1,2));
end