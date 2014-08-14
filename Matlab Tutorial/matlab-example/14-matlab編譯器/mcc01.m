fprintf('MATLAB version=%s\n', version);
if ~exist('myEigValue'), mkdir('myEigValue'); end
fprintf('Compiling myEigValue.m...\n');
tic; mcc -m myEigValue.m -d myEigValue;
fprintf('Time for compiling = %g sec\n', toc);
fprintf('Running DOS command: myEigValue\\myEigValue.exe magic(5)\n');
!myEigValue\myEigValue.exe magic(5)
fprintf('Running DOS command: myEigValue\\myEigValue.exe "[1 2 3; 3 2 1; 1 1 1]"\n');
!myEigValue\myEigValue.exe "[1 2 3; 3 2 1; 1 1 1]"
