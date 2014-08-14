optsFile = [matlabroot '\bin\win64\mexopts\msvc100engmatopts.bat'];
mex('-f', optsFile, 'plotViaMatlab02.c');	% 進行編譯
!plotViaMatlab02				% 測試程式