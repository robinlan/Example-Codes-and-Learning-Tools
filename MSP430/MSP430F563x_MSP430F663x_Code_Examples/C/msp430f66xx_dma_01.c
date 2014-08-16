/* --COPYRIGHT--,BSD_EX
 * Copyright (c) 2012, Texas Instruments Incorporated
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * *  Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * *  Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * *  Neither the name of Texas Instruments Incorporated nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *******************************************************************************
 * 
 *                       MSP430 CODE EXAMPLE DISCLAIMER
 *
 * MSP430 code examples are self-contained low-level programs that typically
 * demonstrate a single peripheral function or device feature in a highly
 * concise manner. For this the code may rely on the device's power-on default
 * register values and settings such as the clock configuration and care must
 * be taken when combining code from several examples to avoid potential side
 * effects. Also see www.ti.com/grace for a GUI- and www.ti.com/msp430ware
 * for an API functional library-approach to peripheral configuration.
 *
 * --/COPYRIGHT--*/
//******************************************************************************
//  MSP430F66x Demo - DMA0, Repeated Block to-from RAM, Software Trigger
//
//  Description: A 16 word block from 1C00-1C1Fh is transfered to 1C20h-1C3fh
//  using DMA0 in a burst block using software DMAREQ trigger.
//  After each transfer, source, destination and DMA size are
//  reset to inital software setting because DMA transfer mode 5 is used.
//  P1.0 is toggled durring DMA transfer only for demonstration purposes.
//  ** RAM location 0x1C00 - 0x1C3F used - make sure no compiler conflict **
//  ACLK = REFO = 32kHz, MCLK = SMCLK = default DCO 1048576Hz
//  Use large memory model
//
//                 MSP430x66x
//             -----------------
//         /|\|              XIN|-
//          | |                 | 32kHz
//          --|RST          XOUT|-
//            |                 |
//            |             P1.0|-->LED
//
//   Priya Thanigai
//   Texas Instruments Inc.
//   Nov 2009
//   Built with IAR Embedded Workbench Version: 4.20 & Code Composer Studio V4.0
//******************************************************************************
#include <msp430.h>

int main(void)
{
  WDTCTL = WDTPW+WDTHOLD;                   // Stop WDT
  P1DIR |= 0x01;                            // P1.0 output
  __data16_write_addr((unsigned short) &DMA0SA,(unsigned long) 0x3400);
                                            // Source block address
  __data16_write_addr((unsigned short) &DMA0DA,(unsigned long) 0x3420);
                                            // Destination single address
  DMA0SZ = 16;                              // Block size
  DMA0CTL = DMADT_5+DMASRCINCR_3+DMADSTINCR_3; // Rpt, inc
  DMA0CTL |= DMAEN;                         // Enable DMA0

  while(1)
  {
    P1OUT |= 0x01;                          // P1.0 = 1, LED on
    DMA0CTL |= DMAREQ;                      // Trigger block transfer
    P1OUT &= ~0x01;                         // P1.0 = 0, LED off
  }
}
