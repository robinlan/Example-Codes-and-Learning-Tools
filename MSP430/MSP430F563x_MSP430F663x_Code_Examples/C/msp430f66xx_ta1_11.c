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
//  MSP430F66x Demo - Timer1_A3, Toggle P1.7/TA1.0, Up Mode, 32kHz ACLK
//
//  Description: Toggle P3.1 using hardware TA1.0 output. Timer1_A is configured
//  for up mode with CCR0 defining period, TA1.0 also output on P3.1. In this
//  example, CCR0 is loaded with 10-1 and TA1.0 will toggle P3.1 at TACLK/10.
//  Thus the output frequency on P3.1 will be the TACLK/20. No CPU or software
//  resources required. Normal operating mode is LPM3.
//  As coded with TACLK = ACLK, P3.1 output frequency = 32768/20 = 1.6384kHz.
//  ACLK = TACLK = 32kHz, MCLK = default DCO ~1.045MHz
//
//                MSP430F66x
//            -------------------
//        /|\|                   |
//         | |                   |
//         --|RST                |
//           |                   |
//           |         P3.1/TA1.0|--> ACLK/20
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
  while(BAKCTL & LOCKBAK)                    // Unlock XT1 pins for operation
     BAKCTL &= ~(LOCKBAK); 
  UCSCTL6 &= ~(XT1OFF);                     // XT1 On
  UCSCTL6 |= XCAP_3;                        // Internal load cap 
  // Loop until XT1 fault flag is cleared
  do
  {
    UCSCTL7 &= ~(XT2OFFG + XT1LFOFFG + DCOFFG);
                                            // Clear XT2,XT1,DCO fault flags
    SFRIFG1 &= ~OFIFG;                      // Clear fault flags
  }while (SFRIFG1&OFIFG);                   // Test oscillator fault flag
  
  P3DIR |= BIT1;                            // P3.1 output
  P3SEL |= BIT1;                            // P3.1 option select
  TA1CCTL0 = OUTMOD_4;                      // CCR0 toggle mode
  TA1CCR0 = 10-1;
  TA1CTL = TASSEL_1 + MC_1 + TACLR;         // ACLK, upmode, clear TAR

  __bis_SR_register(LPM3_bits);             // Enter LPM3
  __no_operation();                         // For debugger
}
