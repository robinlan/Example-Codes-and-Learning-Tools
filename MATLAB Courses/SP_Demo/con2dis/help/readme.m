%===========================================================================
% Contineous to Discrete Demo  (con2dis.m)
%===========================================================================
%
%  
%
% Installation Instructions:
% --------------------------
%    There are no special installation instructions required.  The archive
%    just needs to be unpacked with the original directory structure 
%    preserved.
%
% To Run:
% -------
%    The GUI is started by running the con2dis.m file.  For further help,
%    use the help menu.
%
% Contact Information:
% --------------------
% If you find and wish to report a bug or have any questions you can contact
%
%   James H. McClellan
%   james.mcclellan@ece.gatech.edu
%
%
%===========================================================================
% Revision Summary
%===========================================================================
%---------------------------------------------------------------------------
% Con2Dis ver 0.96 (5-October-2001, Jim McClellan)
%---------------------------------------------------------------------------
% Con2Dis ver 0.97 (20-October-2001, Gregory Krudysz)
% 		: Code from 'Initialize' was brken up into two new functions 
%      defaultplots.m and defaultplots.m 
%    : Added 'ResizeFigure' within con2dis.m 
%    : Took out '^' from xlabel under h.Axis3, due to figure resize problems 
%----------------------------------------------------------------------------
% Known Problems:
% Unix: Font xlabel under h.Axis2 differs from other fonts
%       Font size is too small
% All : When fs is changed and Rad/sec radio button is checked, Axis3
%		  xtick label is overwritten