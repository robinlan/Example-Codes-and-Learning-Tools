mFiles=dir('string*.m');
mFiles={mFiles.name};

for i=1:length(mFiles)
	% ====== Execute each command
	command=mFiles{i}(1:end-2);
	fprintf('%d/%d: %s\n', i, length(mFiles), command);
	outputFile=['output/', command, '.txt'];
	outImgFile=['output/', command, '.jpg'];
	delete(outputFile);
	diary(outputFile);
	eval(command);
	diary off;
	% ====== Get rid of empty line
	clear content
	content=fileRead(outputFile);
	index=[];
	for j=1:length(content)
		if isempty(content{j})
			index=[index, j];
		end
	end
	content(index)=[];
	% ====== Write output file
	fileWrite(content, outputFile);
	% ====== Write image file
	mContent=fileRead(mFiles{i});
	usePlot=0;
	for j=1:length(mContent),
		if ~isempty(findstr(mContent{j}, 'plot')), usePlot=1; break; end
	end
	if usePlot,
		print('-djpeg', outImgFile);
	end
end
