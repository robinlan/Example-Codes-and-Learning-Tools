/*
 * bcd_test.c - example to show use of bcd conversion routines
 *
 *  Author: kimballr
 *  Date: Feb 22, 2013
 *  Version: 0.0001
 *
 * Compiled like this:
 *
 * $ msp430-gcc -std=c99 -DF_CPU=16000000 -g -Os -Wall -Wunused \
 *    -fdata-sections -ffunction-sections -fwrapv -mmcu=msp430g2553 \
 *    -Wl,--gc-sections,-Map=bcd_test.map -mdisable-watchdog \
 *    bcd_test.c bcdutils.S \
 *    -o bcd_test.out
 *
 * $ mspdebug rf2500 "prog bcd_test.out"
 *
 */

#include <msp430.h>
#include <stdint.h>
#include <stdlib.h>
#include "bcdutils.h"


typedef void (*cb_func)(const char);

/**
 * UART serial output routines
 */

void printc(const char c) {
  while (!(IFG2 & UCA0TXIFG))
    ;
  IFG2 &= ~UCA0TXIFG;
  UCA0TXBUF = c;
}

void println() {
  printc('\n');
}

/*
 * bcd16toasc(bcd, write_cb)
 */
void bcd16toasc(long bcd, cb_func write_func) {
  if ( bcd > 0 ) {
    unsigned shft = 20;
    unsigned long mask=0xF0000;

    // strip leading zeros
    while(!(bcd & mask)) {
      shft-=4, mask>>= 4;
    }

    // convert nibble at a time 0-9 binary to ascii '0'-'9'
    do {
      shft -= 4;
      write_func((bcd >> shft & 0xF) + '0');
    } while( shft );
  }
  else {
    printc('0');
  }
}

void printbcd16(const unsigned long u) {
  if ( u > 0 ) {
    unsigned shft = 20;
    unsigned long mask=0xF0000;

    // strip leading zeros
    while(!(u & mask)) shft-=4, mask>>= 4;

    // convert nibble at a time 0-9 binary to ascii '0'-'9'
    do {
      shft -= 4;
      printc((u >> shft & 0xF) + '0');
    } while( shft );
  }
  else {
    printc('0');
  }
}

void printbcd32(const unsigned long long u) {
  if ( u > 0 ) {
    unsigned shft = 40;
    unsigned long long mask=0xF000000000;

    // strip leading zeros
    while(!(u & mask)) shft-=4,mask>>= 4;

    // convert nibble at a time 0-9 binary to ascii '0'-'9'
    do {
      shft -= 4;
      printc((u >> shft & 0xF) + '0');
    } while( shft );
  }
  else {
    printc('0');
  }
}

void prints(const char *s) {
  while (*s)
    printc(*s++);
}

void printu(uint16_t i) {
  long n = u16tobcd(i);
  bcd16toasc(n, printc);
}

void printi(int16_t i) {
  if (i < 0) {
    printc('-');
    i = -i;
  }
  printu(i);
}

void printul(uint32_t i) {
  long long n = u32tobcd(i);
  printbcd32(n);
}

void printl(int32_t i) {
  if (i < 0) {
    printc('-');
    i = -i;
  }
  printul(i);
}

#define F_CPU 16000000

static const uint32_t brd = (F_CPU + (9600 >> 1)) / 9600; // Bit rate divisor

void main(void) {
  WDTCTL = WDTPW + WDTHOLD;

  // run @ 16MHz
  BCSCTL1 = CALBC1_16MHZ;
  DCOCTL = CALDCO_16MHZ;

  // UART configuration 9600-8-1-N
  P1SEL |= BIT2;   P1SEL2 |= BIT2; // TX pin
  UCA0CTL1 = UCSWRST;
  UCA0CTL0 = 0;
  UCA0BR1 = (brd >> 12) & 0xFF;
  UCA0BR0 = (brd >> 4) & 0xFF;
  UCA0MCTL = ((brd << 4) & 0xF0) | UCOS16;
  UCA0CTL1 = UCSSEL_2;

  unsigned u16;

  println();

  for(u16=32767u-5; u16 <= 32767u+5; u16++ ) {
    printu(u16);
    printc(',');
    printi(u16);println();
  }

#if 0
  unsigned long u32;

  for(u32=2147483647ul-5; u32 <= 2147483647ul+5; u32++ ) {
    printul(u32);
    printc(',');
    printl(u32);
    println();
  }

  for(u16=0; u16 <= 65535U; u16++ ) {
    printu(u16);
    printc(',');
    printi(u16);println();
  }
#endif
  LPM4;
}
