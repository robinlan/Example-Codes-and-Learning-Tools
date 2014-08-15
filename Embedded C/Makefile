########################################################################
#
# Filename:     Makefile
# 
# Description:  Highest-level build instructions (for the entire book).
# 
# Notes:        
#  
#  
# Copyright (c) 1998 by Michael Barr.  This software is placed into
# the public domain and may be used for any purpose.  However, this
# notice must not be changed or removed and no warranty is either
# expressed or implied by its publication or distribution.
########################################################################


.AUTODEPEND


########################################################################
# Directory Names
#
BCCHOME  = $(MAKEDIR)\..         # Borland Home Directory
BCCINC   = $(BCCHOME)\include    # Include Files Path

SVHOME   = C:\TOOLS\SV188EB
SVINC    = $(SVHOME)


########################################################################
# Run-Time Library
#
PROCESSOR = 188EB                 # Processor Type
RTOS      = 0                     # Commercial RTOS Support?
MODEL     = l                     # c=compact s=small m=medium l=large h=huge

ARCOMLIB  = $(SVHOME)\$(PROCESSOR)$(RTOS)$(MODEL).lib


########################################################################
# Development Tools
#
CC        = bcc       
INCLUDE   = .;Chapter2;Chapter5;Chapter7;Chapter8;Chapter9;$(SVINC);$(BCCINC)
CFLAGS    = -c -v -m$(MODEL) -1 -K -I$(INCLUDE)
BCCLIB    = $(BCCHOME)\lib\c$(MODEL)

AS        = tasm
AFLAGS    = /mx

LD        = tlink    
LFLAGS    = -m -v -s  

RM        = erase
CP        = copy


########################################################################
# Production Rules
#
default: all


########################################################################
# Subsystem Makefiles
#
!include Chapter2\module.mk
!include Chapter6\module.mk
!include Chapter7\module.mk
!include Chapter9\module.mk


########################################################################
# Shared Production Rules
#
all: $(CHAPTER2) $(CHAPTER6) $(CHAPTER7) $(CHAPTER9)


########################################################################
# Automatic Rules
#
.asm.obj:
	$(AS) $(AFLAGS) $<, $*.obj 

.c.obj:
	$(CC) @&&!
				$(CFLAGS)
				-o$*.obj $< 
!

.cpp.obj:
	$(CC) @&&!
				$(CFLAGS)
				-o$*.obj $<
!
