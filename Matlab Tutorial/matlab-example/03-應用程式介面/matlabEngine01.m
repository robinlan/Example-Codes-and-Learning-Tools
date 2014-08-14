optsFile = [matlabroot '\bin\win64\mexopts\msvc100engmatopts.bat'];
mex('-f', optsFile, 'plotViaMatlab01.c');	% 進行編譯
!plotViaMatlab01				% 測試程式