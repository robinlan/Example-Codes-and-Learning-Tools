function installcheck()
%INSTALLCHECK Check the current installation.
%   A common error when unpacking the archive of program
%   files is to dump all the files into a single directory
%   instead of unpacking it with the proper directory 
%   hierarchy.  This function provides a simple check
%   to see if this occurred.
%
%   NOTE: To work this function MUST be placed in a directory
%   called private and called from the parent directory.
%
%   This function simply checks to see if it is contained
%   in a directory called private.  If so, it is more than
%   likely that the archive was unpacked correctly and no 
%   error is generated.  If it is not inside a private directory
%   then an error is generated which suggests to the user
%   a possible cause for the installation error.

% Jordan Rosenthal, 25-Mar-2000
% jr@ece.gatech.edu

% 12-Nov-2002: Rev 1.1 - Updated to use 'dbstack' instead of mfilename, 
%              due to change in 'mfilename' in Matlab 6.5
% Rajbabu Velmurugan

[st, i] = dbstack;
k = findstr('private', st(1).name);
if isempty(k),
   msg = ['This program has not been installed correctly. ', ...
         'The most probable cause of the problem is that the files ', ...
         'from the compressed archive were extracted into a single ', ...
         'directory instead of the retaining the original directory ', ...,
         'hierarchy.  If only a single directory of files currently ', ...
         'exists, please unpack the archive again using the retain ', ...
         'folders option in your decompression program. '];
   error(msg);
end

%end function

%eof: installcheck.m

