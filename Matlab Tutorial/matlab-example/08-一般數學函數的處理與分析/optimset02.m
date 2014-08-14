options = optimset('fminbnd');
fieldNames=fieldnames(options);
fprintf('No. of fields = %d\n', length(fieldNames));
for i=1:length(fieldNames)
	if ~isempty(options.(fieldNames{i}))
		fprintf('options.%s=%s\n', fieldNames{i}, num2str(options.(fieldNames{i})));
	end
end