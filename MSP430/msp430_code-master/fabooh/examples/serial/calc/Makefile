#
# fabooh example makefile
#

TARGET?=calc
LDLIBS?=
USEROBJS=scanner.o
USERCLEAN=scanner.cpp

include ../../../common.mk

scanner.cpp: scanner.re
	re2c -s -o $@ $<

