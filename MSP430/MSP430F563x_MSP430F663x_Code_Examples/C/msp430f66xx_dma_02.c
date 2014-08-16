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
//  MSP430x66x Demo - DMA0, Repeated Block UCA1UART 9600, TACCR2, ACLK
//
//  Description: DMA0 is used to transfer a string byte-by-byte as a repeating
//  block to UCA1TXBUF. Timer_A operates continuously at 32768Hz with CCR2IFG
//  triggering DMA0. "Hello World" is TX'd via 9600 baud UART1.
//  ACLK= TACLK 32768Hz, MCLK= SMCLK= default DCO ~ 1048576Hz
//  Baud rate divider with 32768hz XTAL @9600 = 32768Hz/9600 = 3.41 (000Dh 4Ah )
//  //* An external watch crystal on XIN XOUT is required for ACLK *//	
//
//              MSP430x66x
//            -----------------
//        /|\|              XIN|-
//         | |                 | 32768Hz
//         --|RST          XOUT|-
//           |                 |
//           |             P4.4|------------> "Hello World"
//           |                 | 9600 - 8N1
//
//
//   Priya Thanigai
//   Texas Instruments Inc.
//   Nov 2009
//   Built with IAR Embedded Workbench Version: 4.20 & Code Composer Studio V4.0
//******************************************************************************
#include <msp430.h>

static char String1[] = { "Hello World\r\n" };

int main(void)
{
  WDTCTL = WDTPW + WDTHOLD;                 // Stop watchdog

  UCSCTL6 &= ~(XT1OFF);                     // XT1 On
  UCSCTL6 |= XCAP_3;                        // Internal load cap
  UCSCTL3 = 0;                              // FLL Reference Clock = XT1

  // Loop until XT1,XT2 & DCO stabilizes - In this case loop until XT1 and DCo settle
  do
  {
    UCSCTL7 &= ~(XT2OFFG + XT1LFOFFG + XT1HFOFFG + DCOFFG);
                                            // Clear XT2,XT1,DCO fault flags
    SFRIFG1 &= ~OFIFG;                      // Clear fault flags
  }while (SFRIFG1&OFIFG);                   // Test oscillator fault flag
  
  UCSCTL6 &= ~(XT1DRIVE_3);                 // Xtal is now stable, reduce drive strength

  UCSCTL4 |= SELA_0 + SELS_4 + SELM_4;      // ACLK = LFTX1
                                            // SMCLK = default DCO
                                            // MCLK = default DCO

  P8SEL = BIT2+BIT3;                        // P8.2,3 = UART1 TXD/RXD
  // configure USCI_A1 UART
  UCA1CTL1 = UCSSEL_1;                      // ACLK
  UCA1BR0 = 0x03;                           // 32768Hz 9600 32k/9600=3.41
  UCA1BR1 = 0x0;
  UCA1MCTL = UCBRS_3+UCBRF_0;               // Modulation UCBRSx = 3
  UCA1CTL1 &= ~UCSWRST;                     // **Initialize USCI state machine**
  // configure DMA0
  DMACTL0 = DMA0TSEL_1;                     // 0-CCR2IFG
  __data16_write_addr((unsigned short) &DMA0SA,(unsigned long) String1);
                                            // Source block address
  __data16_write_addr((unsigned short) &DMA0DA,(unsigned long) &UCA1TXBUF);
                                            // Destination single address  
  DMA0SZ = sizeof String1-1;                // Block size
  DMA0CTL = DMADT_4 + DMASRCINCR_3 + DMASBDB + DMAEN;// Rpt, inc src, enable

  TA0CCR0 = 8192;                            // Char freq = TACLK/CCR0
  TA0CCR2 = 1;                               // For DMA0 trigger
  TA0CTL = TASSEL_1 + MC_1;                  // ACLK, up-mode

  _BIS_SR(LPM3_bits);                       // Enter LPM3
}

