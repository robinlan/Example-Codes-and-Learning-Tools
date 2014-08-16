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
//  MSP430F66x Demo - DAC12_0, Output Voltage Ramp on DAC0
//
//  Description: Using DAC12_0 and AVcc reference with a gain of 1,
//  output positive ramp on DAC0. Normal mode is LPM0 with CPU off.  WDT used
//  to provide ~61us interrupt used to wake up the CPU and update the DAC
//  with software. 
//  ACLK = n/a, MCLK = SMCLK = default DCO
//
//               MSP430F66x
//            -----------------
//        /|\|              XIN|-
//         | |                 |
//         --|RST          XOUT|-
//           |                 |
//           |        P6.6/DAC0|--> Ramp_positive
//           |                 |
//
//   Priya Thanigai
//   Texas Instruments Inc.
//   Nov 2009
//   Built with IAR Embedded Workbench Version: 4.20 & Code Composer Studio V4.0
//******************************************************************************
#include <msp430.h>


int main(void)
{
  WDTCTL = WDT_MDLY_0_064;                  // WDT ~61us interval timer
  SFRIE1 = WDTIE;                           // Enable WDT interrupt

  DAC12_0CTL0 = DAC12IR + DAC12SREF_0 + DAC12AMP_5 + DAC12ENC + DAC12CALON; 

  for (;;)
  {
    __bis_SR_register(CPUOFF + GIE);        // Enter LPM0
    DAC12_0DAT++;                           // Positive ramp
    DAC12_0DAT &= 0xFFF;                    // Modulo 4096
  }
}

#pragma vector=WDT_VECTOR
__interrupt void watchdog_timer (void)
{
   __bic_SR_register_on_exit(CPUOFF);       // Clear LPM0 bits from 0(SR)
}
