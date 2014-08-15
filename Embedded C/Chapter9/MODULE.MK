########################################################################
#
# Filename:     module.mk
# 
# Description:  Build instructions for the examples in Chapter 9.
# 
# Notes:        The order in which the object files are linked is
#               important.  Chapter3\startup.obj must be linked
#               first and Chapter8\ostest.obj must be linked last.
#  
#  
# Copyright (c) 1998 by Michael Barr.  This software is placed into
# the public domain and may be used for any purpose.  However, this
# notice must not be changed or removed and no warranty is either
# expressed or implied by its publication or distribution.
########################################################################


CHAPTER9 = Chapter9\hello.exe

Chapter9\hello.exe: Chapter3\startup.obj  Chapter5\i8018xEB.obj \
                    Chapter8\bsp.obj      Chapter8\task.obj	 	\
                    Chapter8\sched.obj    Chapter8\mutex.obj 	\
                    Chapter9\led.obj      Chapter9\timer.obj    \
					Chapter9\serial.obj   Chapter9\circbuf.obj	\
					Chapter9\scc.obj	  Chapter9\hello.obj
    $(RM) $@
	$(LD) $(LFLAGS) @&&!
						$?
						$@
						$*.map
						$(ARCOMLIB) math$(MODEL) $(BCCLIB)
!
