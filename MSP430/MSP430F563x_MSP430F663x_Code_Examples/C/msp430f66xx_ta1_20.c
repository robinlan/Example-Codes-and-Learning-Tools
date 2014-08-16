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
//*******************************************************************************
//  MSP430F66x Demo - Timer1_A3, PWM TA1.1-2, Up/Down Mode, 32kHz ACLK
//
//  Description: This program generates two PWM outputs on P2.0, P2.1 using
//  Timer1_A configured for up/down mode. The value in CCR0, 128, defines the
//  PWM period/2 and the values in CCR1 and CCR2 the PWM duty cycles. Using
//  32kHz ACLK as TACLK, the timer period is 7.8ms with a 75% duty cycle
//  on P2.0 and 25% on P2.1. Normal operating mode is LPM3
//  ACLK = TACLK = LFXT1 = 32768Hz, MCLK = default DCO ~1.045MHz.
//
//                MSP430F66x
//            -------------------
//        /|\|                   |
//         | |                   |
//         --|RST                |
//           |                   |
//           |         P3.2/TA1.1|--> CCR1 - 75% PWM
//           |         P3.3/TA1.2|--> CCR2 - 25% PWM
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
  
  P3DIR |= BIT2+BIT3;                       // P3.2 and P3.3 output
  P3SEL |= BIT2+BIT3;                       // P3.2 and P3.3 options select
  TA1CCR0 = 128;                            // PWM Period/2
  TA1CCTL1 = OUTMOD_6;                      // CCR1 toggle/set
  TA1CCR1 = 32;                             // CCR1 PWM duty cycle
  TA1CCTL2 = OUTMOD_6;                      // CCR2 toggle/set
  TA1CCR2 = 96;                             // CCR2 PWM duty cycle
  TA1CTL = TASSEL_1 + MC_3;                 // ACLK, up-down mode

  __bis_SR_register(LPM3_bits);             // Enter LPM3
  __no_operation();                         // For debugger
}

