########################################################################
#
# Filename:     module.mk
# 
# Description:  Build instructions for the examples in Chapter 6.
# 
# Notes:        
#  
#  
# Copyright (c) 1998 by Michael Barr.  This software is placed into
# the public domain and may be used for any purpose.  However, this
# notice must not be changed or removed and no warranty is either
# expressed or implied by its publication or distribution.
########################################################################


CHAPTER6 = Chapter6\memtest.exe Chapter6\crc.exe Chapter6\flash.exe

Chapter6\memtest.exe: Chapter3\startup.obj Chapter6\memtest.obj Chapter2\led.obj
    $(RM) $@
	$(LD) $(LFLAGS) @&&!
						$?
						$@
						$*.map
						$(ARCOMLIB) math$(MODEL) $(BCCLIB)
!

Chapter6\crc.exe: Chapter3\startup.obj Chapter6\crc.obj Chapter2\led.obj
    $(RM) $@
	$(LD) $(LFLAGS) @&&!
						$?
						$@
						$*.map
						$(ARCOMLIB) math$(MODEL) $(BCCLIB)
!

Chapter6\flash.exe: Chapter3\startup.obj Chapter6\flash.obj Chapter2\led.obj
    $(RM) $@
	$(LD) $(LFLAGS) @&&!
						$?
						$@
						$*.map
						$(ARCOMLIB) math$(MODEL) $(BCCLIB)
!
