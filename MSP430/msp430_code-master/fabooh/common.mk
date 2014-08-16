#
# common.mk - common include used by fabooh example makefiles
#
# Created: Feb 28, 2012
#  Author: rick@kimballsoftware.com
#    Date: 03-12-2012
# Version: 1.0.3
#
SELF_DIR := $(dir $(lastword $(MAKEFILE_LIST)))
FABOOH_DIR?=$(patsubst %/,%,$(SELF_DIR))
FABOOH_PLATFORM?=$(FABOOH_DIR)/msp430

# pick a board, just uncomment one of the includes below

#include $(FABOOH_DIR)/mkfiles/include-msp430g2231in14.mk
include $(FABOOH_DIR)/mkfiles/include-msp430g2553in20.mk
#include $(FABOOH_DIR)/mkfiles/include-msp430fr5739.mk

# users can provide additional command line CFLAGS by
# setting: $ make FLAGS="gcc extra flags here"
# or by putting it in their makefiles
FLAGS?=
USEROBJS?=
LDLIBS?=

# tools we use
CC = msp430-gcc
LD = msp430-gcc
MSPDEBUG = mspdebug
MSP430_STDLST = $(FABOOH_DIR)/tools/msp430-stdlst

FIXMATH_FLAGS=-DFIXMATH_NO_CACHE -DFIXMATH_NO_64BIT -DFIXMATH_NO_ROUNDING

INCLUDE=-I $(FABOOH_PLATFORM)/cores/$(CORE) \
		-I $(FABOOH_PLATFORM)/variants/$(BOARD) \
		-I $(FABOOH_PLATFORM)/cores/$(CORE)/drivers \
		-I $(FABOOH_PLATFORM)/third_party

CFLAGS= -g -Os -Wall -Wunused -mdisable-watchdog \
 		-fdata-sections -ffunction-sections -MMD \
		-fwrapv -fomit-frame-pointer \
		-mmcu=$(MCU) -DF_CPU=$(F_CPU) $(INCLUDE) \
		$(FIXMATH_FLAGS) \
		$(STACK_CHECK) $(FLAGS)

ASFLAGS = $(CFLAGS)

CCFLAGS = $(CFLAGS) -std=gnu++98 

LDFLAGS=$(LDPRE) -g -mmcu=$(MCU) -mdisable-watchdog \
		-Wl,--gc-sections,-Map=$(TARGET).map,-umain $(LDPOST)

OBJECTS?=$(TARGET).o $(USEROBJS)

DEPFILES:=$(patsubst %.o,%.d,$(OBJECTS))

NODEPS:=clean
.PHONY: $(NODEPS) install size debug

all: $(TARGET).elf

$(TARGET).elf : $(OBJECTS)
	$(LD) $(LDFLAGS) $(OBJECTS) -o $(TARGET).elf $(LDLIBS)
	$(MSP430_STDLST) $(TARGET).elf

clean:
	@echo "cleaning $(TARGET) ..."
	@rm -f $(OBJECTS) $(TARGET).map $(TARGET).elf
	@rm -f $(DEPFILES) header.tmp
	@rm -f $(TARGET)_asm_mixed.txt
	@rm -f $(TARGET)_asm_count.txt
	@rm -f $(TARGET).hex
	@rm -f $(TARGET).map
ifdef USERCLEAN
	rm -f $(USERCLEAN)
endif
 
install: all
	$(MSPDEBUG) rf2500 "prog $(TARGET).elf"
 
size: all
	msp430-size $(TARGET).elf

debug: $(TARGET).elf
	xterm -fn 8x13 -e 'mspdebug rf2500 gdb' &
	sleep 2; gnome-terminal -x bash -c "msp430-gdb -ex='target remote:2000' $(TARGET).elf" &
	
%.o : %.cpp
	$(CC) $(CCFLAGS) -c $<

%.o : %.c
	$(CC) $(CFLAGS) -c $<

%.cpp:	%.ino
	echo "#include <fabooh.h>" > header.tmp
	echo "#line 1 \"$<\"" >>header.tmp
	cat header.tmp $< > $@
	rm header.tmp

%.cpp: %.re
	re2c -s -o $@ $<

ifeq (0, $(words $(findstring $(MAKECMDGOALS), $(NODEPS))))
	#Chances are, these files don't exist.  GMake will create them and
	#clean up automatically afterwards
	-include $(DEPFILES)
endif
