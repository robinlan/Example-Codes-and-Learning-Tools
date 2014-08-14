function interpolation(action)
%interploation: Interactive plot for various methods of interpolation

%	Roger Jang, 20041225, 20120511

persistent xSample ySample xx yy nearestIndex grabbed sampleH interpH method

if nargin<1, action='start'; end

switch(action)
	case 'start'	% 開啟圖形視窗
		xSample = linspace(1, 10, 10);
		ySample = [10 7 5 4 3.5 3.2 2 1 2 4];
		method={'nearest', 'linear', 'spline', 'pchip'};
		xx = linspace(min(xSample), max(xSample), 201)';
		for i=1:length(method)
			yy(:, i)=interp1(xSample, ySample, xx, method{i});
		end
		figure('name', 'Data Fitting', 'NumberTitle', 'off');
	%	screenSize=get(0, 'screenSize'); set(gcf, 'position', [1, 50, screenSize(3), screenSize(4)/2]);
		interpH = plot(xx, yy, '-');
		sampleH = line(xSample, ySample, 'marker', 'o', 'color', 'k', 'lineStyle', 'none');
		limit = [min(xSample) max(xSample) min(ySample)-1 max(ySample)+1]; axis(limit);
		legend(method);
		xlabel('Click and drag a sample point to change the curves.');
		title('Animation of various methods for interpolation');
		grabbed=0;

		% 設定滑鼠按下時的反應指令
		set(gcf, 'WindowButtonDownFcn', sprintf('%s(%s)', mfilename, '''down'''));
		% 設定滑鼠移動時的反應指令
		set(gcf, 'WindowButtonMotionFcn', sprintf('%s(%s)', mfilename, '''move'''));
	case 'nearASamplePoint'		% 只改變 nearestIndex
		currPt=get(gca, 'CurrentPoint'); xPos=currPt(1,1); yPos=currPt(1,2);
		xlim=get(gca, 'xlim'); ylim=get(gca, 'ylim');
		distTh=max(xlim(2)-xlim(1), ylim(2)-ylim(1))/50;	% Distance threshold
		if xlim(1)<=xPos & xPos<=xlim(2) & ylim(1)<=yPos & yPos<=ylim(2)
			dist=zeros(1, length(xSample));
			for i=1:length(dist)
				dist(i)=sqrt((xPos-xSample(i))^2+(yPos-ySample(i))^2);
			end
			[minDist, minIndex]=min(dist);
			if minDist<=distTh
				nearestIndex=minIndex;
			else
				nearestIndex=[];
			end
		end
	case 'down'	% 滑鼠按鈕被按下時的反應指令
		% 設定滑鼠按鈕被釋放時的反應指令
		set(gcf, 'WindowButtonUpFcn', sprintf('%s(%s)', mfilename, '''up'''));
		feval(mfilename, 'nearASamplePoint');
		if ~isempty(nearestIndex);
			grabbed=1;	% 抓到一個點了
			set(gcf, 'pointer', 'fleur');
		end
	case 'move'	% 滑鼠移動時的反應指令
		currPt=get(gca, 'CurrentPoint'); xPos=currPt(1,1); yPos=currPt(1,2);
		if grabbed	% 抓到樣本點之後的滑動
			xSample(nearestIndex)=xPos;
			ySample(nearestIndex)=yPos;
			set(sampleH, 'xData', xSample, 'yData', ySample);
			for i=1:length(method)
				yy(:, i)=interp1(xSample, ySample, xx, method{i});
				set(interpH(i), 'ydata', yy(:, i));
			end
		else		% 抓到樣本點之前的滑動
			feval(mfilename, 'nearASamplePoint');
			if ~isempty(nearestIndex);
				set(gcf, 'pointer', 'hand');
			else
				set(gcf, 'pointer', 'arrow');
			end
		end
	case 'up'	% 滑鼠按鈕被釋放時的反應指令
		% 清除滑鼠移動時的反應指令
		%set(gcf, 'WindowButtonMotionFcn', '');
		% 清除滑鼠按鈕被釋放時的反應指令
		set(gcf, 'WindowButtonUpFcn', '');
		set(gcf, 'pointer', 'arrow');
		grabbed=0;
end