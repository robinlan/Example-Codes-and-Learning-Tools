x = magic(5);
[colMax, colMaxIndex] = max(x);
[maxValue, maxIndex] = max(colMax);
fprintf('Max value = x(%d, %d) = %d\n', ...
	colMaxIndex(maxIndex), maxIndex, maxValue);