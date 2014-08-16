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
//   MSP430F66x Demo - Timer_B, PWM TB1-6, Up Mode, DCO SMCLK
//
//   Description: This program generates six PWM outputs on P4.1-6 using
//   Timer_B configured for up mode. The value in CCR0, 512-1, defines the PWM
//   period and the values in CCR1-6 the PWM duty cycles. Using ~1048kHz SMCLK
//   as TBCLK, the timer period is ~488us.
//   ACLK = 32kHz, SMCLK = MCLK = TBCLK = default DCO ~1048kHz.
//
//                MSP430F66x
//             -----------------
//         /|\|              XIN|-
//          | |                 |  32kHz
//          --|RST          XOUT|-
//            |                 |
//            |         P4.1/TB1|--> CCR1 - 75% PWM
//            |         P4.2/TB2|--> CCR2 - 25% PWM
//            |         P4.3/TB3|--> CCR3 - 12.5% PWM
//            |         P4.4/TB4|--> CCR4 - 6.26% PWM
//            |         P4.5/TB5|--> CCR5 - 3.13% PWM
//            |         P4.6/TB6|--> CCR6 - 1.566% PWM
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
  
  P4SEL |= BIT1+BIT2+BIT3+BIT4+BIT5+BIT6;   // P4.1-4.6 option select
  P4DIR |= BIT1+BIT2+BIT3+BIT4+BIT5+BIT6;   // P4.1-4.6 outputs 

  TBCCR0 = 512-1;                           // PWM Period
  TBCCTL1 = OUTMOD_7;                       // CCR1 reset/set
  TBCCR1 = 383;                             // CCR1 PWM Duty Cycle	
  TBCCTL2 = OUTMOD_7;                       // CCR2 reset/set
  TBCCR2 = 128;                             // CCR2 PWM duty cycle	
  TBCCTL3 = OUTMOD_7;                       // CCR3 reset/set
  TBCCR3 = 64;                              // CCR3 PWM duty cycle	
  TBCCTL4 = OUTMOD_7;                       // CCR4 reset/set
  TBCCR4 = 32;                              // CCR4 PWM duty cycle	
  TBCCTL5 = OUTMOD_7;                       // CCR5 reset/set
  TBCCR5 = 16;                              // CCR5 PWM duty cycle	
  TBCCTL6 = OUTMOD_7;                       // CCR6 reset/set
  TBCCR6 = 8;                               // CCR6 PWM duty cycle	
  TBCTL = TBSSEL_2+MC_1;                    // SMCLK, upmode

  _BIS_SR(LPM0_bits + GIE);                 // CPU off
}
