//******************************************************************************
//  MSP430x54x Demo - Bank Erase from a Block while Executing Code from Another
//                    Block.
//
//  Description: This program first writes to Bank 1 from 0x10000 to 0x10100.
//  Then a bank erase is done on Bank 1. During Bank erase, code is still
//  executing from Bank 0 by toggling P1.0. Toggling can only be seen by using
//  an oscilloscope.
//  Ensure Large memory model is used.
//  RESET the device to re-execute code. This is implemented to prevent
//  stressing of Flash unintentionally.
//  ACLK = REFO = 32kHz, MCLK = SMCLK = default DCO 1048576Hz
//
//               MSP430x54x
//            -----------------
//        /|\|              XIN|-
//         | |                 |
//         --|RST          XOUT|-
//           |                 |
//           |             P1.0|- Probe with oscilloscope
//
//  W. Goh
//  Texas Instruments Inc.
//  April 2009
//  Built with CCE v3.1 Build 3.2.3.6.4 and IAR Embedded Workbench v4.11B
//******************************************************************************

#include "msp430x54x.h"

// Function prototypes
void erase_Seg(void);
void wrt_Bank1(void);

char value;                                 // 8-bit value to write to Bank 1

void main(void)
{
  WDTCTL = WDTPW+WDTHOLD;                   // Stop WDT

  P1DIR |= 0x01;                            // Set P1.0 as output

  P1OUT &= ~ 0x01;                          // clear P1.0
  value = 0;                                // SET BREAKPOINT HERE
  wrt_Bank1();                              // Write to Bank 1
  erase_Seg();                              // Call erase function
  while(BUSY & FCTL3)                       // Check for erase completion
  {
    P1OUT ^= 0x01;                          // toggle LED if still erasing
  }                                         // Else, Exit if erase is done

  P1OUT &= ~0x01;                           // Clear LED
  while(1);                                 // Loop forever, SET BREAKPOINT HERE
}

//------------------------------------------------------------------------------
// Erases Memory Bank 1
//------------------------------------------------------------------------------
void erase_Seg(void)
{
  char * Flash_ptr = (char *)0x10000;       // Initialize Flash pointer
  __disable_interrupt();                    // 5xx Workaround: Disable global
                                            // interrupt while erasing. Re-Enable
                                            // GIE if needed
  FCTL3 = FWKEY;                            // Clear Lock bit
  FCTL1 = FWKEY+MERAS;                      // Set Bank Erase bit
  *Flash_ptr = 0;                           // Dummy erase byte
  FCTL3 = FWKEY+LOCK;                       // Set LOCK bit
}

//------------------------------------------------------------------------------
// Writes incremented Value to Bank 1.
//------------------------------------------------------------------------------
void wrt_Bank1(void)
{
  unsigned int i;
  char * Flash_ptr = (char *)0x10000;       // Initialize Flash pointer

  FCTL3 = FWKEY;                            // Clear Lock bit
  FCTL1 = FWKEY+WRT;                        // Set WRT bit for write operation
  for(i = 0; i < 256; i++)
  {
    *Flash_ptr++ = value++;                   // Write a word to flash
  }
  FCTL1 = FWKEY;                            // Clear WRT bit
  FCTL3 = FWKEY+LOCK;                       // Set LOCK bit
}
