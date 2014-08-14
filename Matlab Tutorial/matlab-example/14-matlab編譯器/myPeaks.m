function myPeaks(n)
%myPeaks: Plot the peaks function

%	Roger Jang, 20120806

if (isstr(n))			% 若輸入是字串，轉成數值
	n=eval(n);
end
peaks(n);
