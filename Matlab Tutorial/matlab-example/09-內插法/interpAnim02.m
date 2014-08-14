function interpolation(action)
%interploation: Interactive plot for various methods of interpolation

%	Roger Jang, 20041225

persistent iSample xSample ySample ii xx yy nearestIndex grabbed sampleH interpH method

if nargin<1, action='start'; end

switch(action)
	case 'start'	% 開啟圖形視窗
		iSample = linspace(1, 10, 10);
		xSample = rand(1, length(iSample));
		ySample = rand(1, length(iSample));
		method={'linear', 'spline', 'pchip'};
		ii = linspace(min(iSample), max(iSample), 201)';
		for i=1:length(method)
			xx(:, i)=interp1(iSample, xSample, ii, method{i});
			yy(:, i)=interp1(iSample, ySample, ii, method{i});
		end

		%figure('name', 'Data Fitting', 'NumberTitle', 'off');
		interpH = plot(xx, yy, '-');
		sampleH = line(xSample, ySample, 'marker', 'o', 'color', 'k', 'lineStyle', 'none');
		limit = [0 1 0 1]; axis(limit);
		legend(method); axis image
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
				xx(:, i)=interp1(iSample, xSample, ii, method{i});
				yy(:, i)=interp1(iSample, ySample, ii, method{i});
				set(interpH(i), 'xdata', xx(:, i));
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
