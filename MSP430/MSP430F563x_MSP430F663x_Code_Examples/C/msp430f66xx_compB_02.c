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
// MSP430x66x Demo - COMPB Toggle from LPM4; input channel CB1;
// 		Vcompare is compared against the internal reference 2.0V
//
// Description: Use CompB (input channel CB1) and internal reference to
//	  determine if input'Vcompare'is high of low.  When Vcompare exceeds 2.0V 
//    CBOUT goes high and when Vcompare is less than 2.0V then CBOUT goes low.
//    Connect P3.0/CBOUT to P1.0 externally to see the LED toggle accordingly.
//                                                   
//                 MSP430x66x
//             ------------------                        
//         /|\|                  |                       
//          | |                  |                       
//          --|RST       P6.1/CB1|<--Vcompare            
//            |                  |                                         
//            |        P3.0/CBOUT|----> 'high'(Vcompare>2.0V); 'low'(Vcompare<2.0V)
//            |                  |  |
//            |            P1.0  |__| LED 'ON'(Vcompare>2.0V); 'OFF'(Vcompare<2.0V)
//            |                  | 
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
  P3DIR |= BIT0;                            // P3.0 output direction
  P3SEL |= BIT0;                            // Select CBOUT function on P3.0/CBOUT

// Setup ComparatorB                           
  CBCTL0 |= CBIPEN + CBIPSEL_1;             // Enable V+, input channel CB1              
  CBCTL1 |= CBPWRMD_1;                      // normal power mode         
  CBCTL2 |= CBRSEL;                         // VREF is applied to -terminal 
  CBCTL2 |= CBRS_3+CBREFL_2;                // R-ladder off; Bandgap voltage amplifier ON and generates 2.0V reference              
  CBCTL3 |= BIT1;                           // Input Buffer Disable @P6.1/CB1    
  CBCTL1 |= CBON;                           // Turn On ComparatorB           

  __delay_cycles(75);                       // delay for the reference to settle

  __bis_SR_register(LPM4_bits);             // Enter LPM4
  __no_operation();                         // For debug 
}
