%demofile = [matlabroot '\extern\examples\eng_mat\engwindemo.c'];
%copyfile(demofile, '.');
optsfile = [matlabroot '\bin\win64\mexopts\msvc100engmatopts.bat'];
fprintf('進行編譯...\n');
mex('-f', optsfile, 'engwindemo.c');
dir engwindemo.exe
fprintf('測試程式...\n');
!engwindemo