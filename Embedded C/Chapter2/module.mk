########################################################################
#
# Filename:     module.mk
# 
# Description:  Build instructions for the examples in Chapter 2.
# 
# Notes:        The constants in this file are specific to Arcom's 
#               Target188EB hardware.
#  
#  
# Copyright (c) 1998 by Michael Barr.  This software is placed into
# the public domain and may be used for any purpose.  However, this
# notice must not be changed or removed and no warranty is either
# expressed or implied by its publication or distribution.
########################################################################


CHAPTER2 = Chapter2\blink.exe

Chapter2\blink.exe: Chapter3\startup.obj Chapter2\blink.obj Chapter2\led.obj
    $(RM) $@
	$(LD) $(LFLAGS) @&&!
						$?
						$@
						$*.map
!
