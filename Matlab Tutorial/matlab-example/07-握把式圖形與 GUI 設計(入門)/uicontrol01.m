h = uicontrol;				% 產生按鈕
set(h, 'String', '請按我！');		% 在按鈕表面加入文字「請按我！」
cmd = 'fprintf(''有人按我一下喔！\n'');';	% 定義按鈕被按後的反應指令
set(h, 'Callback', cmd);			% 設定按鈕的反應指令