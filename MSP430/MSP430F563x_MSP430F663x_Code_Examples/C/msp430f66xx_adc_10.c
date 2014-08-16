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
//  MSP430F66xx Demo - ADC12, Sample A10 Temp and Convert to oC and oF
//
//  Description: A single sample is made on A10 with reference to internal
//  1.5V Vref. Software sets ADC12SC to start sample and conversion - ADC12SC
//  automatically cleared at EOC. ADC12 internal oscillator times sample
//  and conversion. In Mainloop MSP430 waits in LPM4 to save power until
//  ADC10 conversion complete, ADC12_ISR will force exit from any LPMx in
//  Mainloop on reti.
//  ACLK = n/a, MCLK = SMCLK = default DCO ~ 1.045MHz, ADC12CLK = ADC12OSC
//
//  Uncalibrated temperature measured from device to devive will vary do to
//  slope and offset variance from device to device - please see datasheet.
//
//  NOTE:1. REFMSTR bit in REFCTL0 regsiter is reset to allow the ADC12_A reference
//    control regsiters handle the reference setting. Upon resetting the REFMSTR
//    bit, all the settings in REFCTL are 'dont care' and the legacy ADC12  
//    control bits (ADC12REFON, ADC12REF2_5, ADC12TCOFF and ADC12REFOUT) control 
//    the reference system.
//        2. Use the TLV calibrated temperature to measure temperature 
//        (the TLV CALIBRATED DATA IS STORED IN THE INFORMATION SEGMENT, SEE DEVICE DATASHEET)
//
//                MSP430F6638
//             -----------------
//         /|\|              XIN|-
//          | |                 |
//          --|RST          XOUT|-
//            |                 |
//            |A10              |
//
//   F. Chen
//   Texas Instruments Inc.
//   Dec. 2012
//   Built with IAR Embedded Workbench Version: 5.51.1 & Code Composer Studio V5.2.1
//******************************************************************************

#include <msp430.h>

#define CALADC12_15V_30C  *((unsigned int *)0x1A1A)   // Temperature Sensor Calibration-30 C
                                                      //See device datasheet for TLV table memory mapping
#define CALADC12_15V_85C  *((unsigned int *)0x1A1C)   // Temperature Sensor Calibration-85 C

unsigned int temp;
volatile float temperatureDegC;
volatile float temperatureDegF;

int main(void)
{
  WDTCTL = WDTPW + WDTHOLD;                 // Stop WDT
  REFCTL0 &= ~REFMSTR;                      // Reset REFMSTR to hand over control to 
                                            // ADC12_A ref control registers  
  ADC12CTL0 = ADC12SHT0_8 + ADC12REFON + ADC12ON;
                                            // Internal ref = 1.5V
  ADC12CTL1 = ADC12SHP;                     // enable sample timer
  ADC12MCTL0 = ADC12SREF_1 + ADC12INCH_10;  // ADC i/p ch A10 = temp sense i/p
  ADC12IE = 0x001;                          // ADC_IFG upon conv result-ADCMEMO
  __delay_cycles(100);                      // Allow ~100us (at default UCS settings)
                                            // for REF to settle
  ADC12CTL0 |= ADC12ENC;

  while(1)
  {
    ADC12CTL0 &= ~ADC12SC;  
    ADC12CTL0 |= ADC12SC;                   // Sampling and conversion start

    __bis_SR_register(LPM4_bits + GIE);     // LPM0 with interrupts enabled
    __no_operation();

    // Temperature in Celsius. See the Device Descriptor Table section in the
    // System Resets, Interrupts, and Operating Modes, System Control Module
    // chapter in the device user's guide for background information on the
    // used formula.
    temperatureDegC = (float)(((long)temp - CALADC12_15V_30C) * (85 - 30)) /
            (CALADC12_15V_85C - CALADC12_15V_30C) + 30.0f;

    // Temperature in Fahrenheit Tf = (9/5)*Tc + 32
    temperatureDegF = temperatureDegC * 9.0f / 5.0f + 32.0f;
	 
    __no_operation();                       // SET BREAKPOINT HERE
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
  case  6:                                  // Vector  6:  ADC12IFG0
    temp = ADC12MEM0;                       // Move results, IFG is cleared
    __bic_SR_register_on_exit(LPM4_bits);   // Exit active CPU
  case  8: break;                           // Vector  8:  ADC12IFG1
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
