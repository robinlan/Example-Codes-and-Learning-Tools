options = optimset('Disp', 'iter', 'TolX', 0.1);
fieldNames=fieldnames(options);
fprintf('No. of fields = %d\n', length(fieldNames));
options
