#
# fabooh blink makefile
#

TARGET?=adc_temp
LDLIBS?=
USEROBJS=fix16_trig.o fix16.o

#
# minimize size by turning off some memory and flash hungry features
#

THIRD_PARTY_DIR=$(FABOOH_PLATFORM)/third_party

include ../../../common.mk

fix16_trig.o: $(THIRD_PARTY_DIR)/libfixmath/fix16_trig.c
	$(CC) $(CFLAGS) -c $< -o $@

fix16.o: $(THIRD_PARTY_DIR)/libfixmath/fix16.c
	$(CC) $(CFLAGS) -c $< -o $@

