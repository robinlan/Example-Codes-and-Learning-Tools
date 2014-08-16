File: readme.md

Binary Coded Decimal routines using the msp430 'dadd' assembler instruction.

Makefile is configured for an Texas Instruments msp430 launchpad with
an msp430g2553 in the socket. It uses mspdebug to program the chip.

To compile:

$ make clean all install

Open serial terminal "/dev/ttyACM0" with a terminal emulator
configured for 9600 baud 8 bits - 1 stop bit N parity

