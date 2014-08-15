########################################################################
#
# Filename:     module.mk
# 
# Description:  Build instructions for the examples in Chapter 7.
# 
# Notes:        
#  
#  
# Copyright (c) 1998 by Michael Barr.  This software is placed into
# the public domain and may be used for any purpose.  However, this
# notice must not be changed or removed and no warranty is either
# expressed or implied by its publication or distribution.
########################################################################


CHAPTER7 = Chapter7\blink.exe

Chapter7\blink.exe: Chapter3\startup.obj Chapter5\i8018xEB.obj \
					Chapter7\timer.obj Chapter7\led.obj Chapter7\blink.obj
    $(RM) $@
	$(LD) $(LFLAGS) @&&!
						$?
						$@
						$*.map
						$(ARCOMLIB) math$(MODEL) $(BCCLIB)
!
