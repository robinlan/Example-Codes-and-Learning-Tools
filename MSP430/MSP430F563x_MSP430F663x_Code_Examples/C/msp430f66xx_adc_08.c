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
//  MSP430F66x Demo - ADC12, Using A8 and A9 Ext Channels for Conversion
//
//  Description: This example shows how to perform A/D conversion on up to 10
//  external channels by showing that channels A8 and A9 can be used for
//  conversion of external signals when not using these channels as external
//  reference inputs. A sequence of conversions is performed - one
//  conversion on A8 and then one conversion on A9. Each conversion uses AVcc
//  and AVss for the references. The conversion results are stored in ADC12MEM0
//  and ADC12MEM1 respectively and are moved to 'results[]' upon completion of
//  the sequence.
//  Test by applying voltages to pins VREF+/VeREF+ for A8 and VREF-/VeREF- for A9,
//  then setting and running to a break point at the "__bic..." instruction in
//  the ISR. To view the conversion results, open a watch window in debugger and
//  view 'results' or view ADC12MEM0 and ADC12MEM1 in an ADC12 SFR window.
//  This can run even in LPM4 mode as ADC has its own clock
//  *NOTE*  When using channels A8 and A9 for external signals, internal
//  references must be used for the conversions. Refer to figure 18-1 in the
//  MSP430x5xx Family User's Guide.
//
//                MSP430F66x
//             -----------------
//         /|\|                 |
//          | |                 |
//          --|RST              |
//            |                 |
//    Vin1 -->|VeREF+           |
//    Vin2 -->|VREF-/VeREF-     |
//
//
//   Priya Thanigai
//   Texas Instruments Inc.
//   Nov 2009
//   Built with IAR Embedded Workbench Version: 4.20 & Code Composer Studio V4.0
//******************************************************************************

#include <msp430.h>

volatile unsigned int results[2];           // Needs to be global in this example

int main(void)
{
  WDTCTL = WDTPW+WDTHOLD;                   // Stop watchdog timer
  ADC12CTL0 = ADC12ON+ADC12MSC+ADC12SHT0_15;// Turn on ADC12, set sampling time
  ADC12CTL1 = ADC12SHP+ADC12CONSEQ_3;       // Use sampling timer, repeat seq
  ADC12MCTL0 = ADC12INCH_8;                 // ref+=AVcc, channel = A8
  ADC12MCTL1 = ADC12INCH_9+ADC12EOS;        // ref+=AVcc, channel = A9, end seq.
  ADC12IE = 0x02;                           // Enable ADC12IFG.1, end channel
  ADC12CTL0 |= ADC12ENC;                    // Enable conversions

  while(1)
  {
    ADC12CTL0 |= ADC12SC;                   // Start convn - software trigger
    
    __bis_SR_register(LPM4_bits + GIE);     // Enter LPM4, Enable interrupts
    __no_operation();                       // For debugger
  }
}

#pragma vector=ADC12_VECTOR
__interrupt void ADC12ISR (void)
{
  switch(__even_in_range(ADC12IV,34))
  {
  case  0: break;                           // Vector  0:  No interrupt
  case  2: break;                           // Vector  2:  ADC overflow
  case  4: break;                           // Vector  4:  ADC timing overflow
  case  6: break;                           // Vector  6:  ADC12IFG0
  case  8:                                  // Vector  8:  ADC12IFG1
    results[0] = ADC12MEM0;                 // Move results, IFG is cleared
    results[1] = ADC12MEM1;                 // Move results, IFG is cleared
    __bic_SR_register_on_exit(LPM4_bits);   // Exit active, SET BREAKPOINT
  case 10: break;                           // Vector 10:  ADC12IFG2
  case 12: break;                           // Vector 12:  ADC12IFG3
  case 14: break;                           // Vector 14:  ADC12IFG4
  case 16: break;                           // Vector 16:  ADC12IFG5
  case 18: break;                           // Vector 18:  ADC12IFG6
  case 20: break;                           // Vector 20:  ADC12IFG7
  case 22: break;                           // Vector 22:  ADC12IFG8
  case 24: break;                           // Vector 24:  ADC12IFG9
  case 26: break;                           // Vector 26:  ADC12IFG10
  case 28: break;                           // Vector 28:  ADC12IFG11
  case 30: break;                           // Vector 30:  ADC12IFG12
  case 32: break;                           // Vector 32:  ADC12IFG13
  case 34: break;                           // Vector 34:  ADC12IFG14
  default: break; 
  }  
}
