fprintf('MATLAB version=%s\n', version);
if ~exist('sharedLibrary'), mkdir('sharedLibrary'); end
fprintf('Creating shared library myMatLib and put it under sharedLibrary...\n');
tic; mcc -B csharedlib:myMatLib myMatInv.m myMatMultiply.m -d sharedLibrary;
fprintf('Time for creating shared library = %g sec\n', toc);
fprintf('Compiling myMatLibMain.c...\n');
mbuild -IsharedLibrary myMatLibMain.c sharedLibrary/myMatLib.lib
fprintf('Copying myMatLib.dll to the current directory...\n');
copyfile('sharedLibrary/myMatLib.dll', '.');
fprintf('Running DOS command: myMatLibMain.exe...\n');
!myMatLibMain.exe
