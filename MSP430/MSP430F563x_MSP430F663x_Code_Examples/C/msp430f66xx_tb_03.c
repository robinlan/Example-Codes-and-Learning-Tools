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
//  MSP430F66x Demo - Timer_B, Toggle P1.0, Overflow ISR, DCO SMCLK
//
//  Description: Toggle P1.0 using software and Timer_B overflow ISR.
//  In this example an ISR triggers when TB overflows.  TB overflow has the
//  lowest priority. Inside the TB overflow ISR P1.0 is toggled. Toggle rate is
//  approximatlely 7.822Hz = [1.045MHz/FFFFh]/2. Proper use of TBIV interrupt vector
//  generator is shown.
//  ACLK = n/a, MCLK = SMCLK = TBCLK = default DCO ~1.045MHz.
//
//           MSP430F66x
//         ---------------
//     /|\|               |
//      | |               |
//      --|RST            |
//        |               |
//        |           P1.0|-->LED
//
//   Priya Thanigai
//   Texas Instruments Inc.
//   Nov 2009
//   Built with IAR Embedded Workbench Version: 4.20 & Code Composer Studio V4.0
//******************************************************************************
#include <msp430.h>


int main(void)
{
  WDTCTL = WDTPW + WDTHOLD;                 // Stop WDT
  P1DIR |= 0x01;                            // Set P1.0 to output direction
  TBCTL = TBSSEL_2 + MC_2 + TBCLR + TBIE;   // SMCLK, contmode, clear TBR
                                            // enable interrupt

  __bis_SR_register(LPM0_bits + GIE);       // Enter LPM0, enable interrupts
  __no_operation();                         // For debugger
}

// Timer_B7 Interrupt Vector (TBIV) handler
#pragma vector=TIMERB1_VECTOR
__interrupt void TIMERB1_ISR(void)
{
  /* Any access, read or write, of the TBIV register automatically resets the
  highest "pending" interrupt flag. */
  switch( __even_in_range(TBIV,14) )
  {
    case  0: break;                          // No interrupt
    case  2: break;                          // CCR1 not used
    case  4: break;                          // CCR2 not used
    case  6: break;                          // CCR3 not used
    case  8: break;                          // CCR4 not used
    case 10: break;                          // CCR5 not used
    case 12: break;                          // CCR6 not used
    case 14: P1OUT ^= 0x01;                  // overflow
            break;
    default: break;
  }
}

