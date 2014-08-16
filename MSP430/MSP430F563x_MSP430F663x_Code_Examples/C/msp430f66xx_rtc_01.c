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
//   MSP430F66x Demo - RTC_B in real time clock mode
//
//   Description: The RTC_B module is used to set the time, start RTC operation,
//   and read the time from the respective RTC registers. Software will set the
//   original time to 11:59:45 am on Friday October 7, 2011. Then the RTC will
//   be activated through software, and the time will be read out once every 
//   second. The proper handling of the RTC interrupt is shown as well.
//
//  //* An external watch crystal on XIN XOUT is required for ACLK *//	
//   ACLK = 32.768kHz, MCLK = SMCLK = default DCO~1MHz
//
//                MSP430F66xx
//             -----------------
//         /|\|              XIN|-
//          | |                 | 32kHz
//          --|RST          XOUT|-
//            |                 |
//            |             P1.0|--> LED
//
//   F. Chen
//   Texas Instruments Inc.
//   December 2012
//   Built with IAR Embedded Workbench V5.51.1 & Code Composer Studio V5.2.1
//******************************************************************************

#include <msp430.h>

unsigned int Seconds;
unsigned int Minutes;
unsigned int Hours;

int main (void)
{
  WDTCTL = WDTPW + WDTHOLD;                 // Stop WDT
  
  while(BAKCTL & LOCKBAK)                    // Unlock XT1 pins for operation
     BAKCTL &= ~(LOCKBAK);
  
  UCSCTL6 &= ~(XT1OFF);                     // XT1 On
  UCSCTL6 |= XCAP_3;                        // Internal load cap
  
  // Loop until XT1,XT2 & DCO stabilizes - In this case loop until XT1 and DCo settle
  do
  {
    UCSCTL7 &= ~(XT2OFFG + XT1LFOFFG + DCOFFG);
                                            // Clear XT2,XT1,DCO fault flags
    SFRIFG1 &= ~OFIFG;                      // Clear fault flags
  }while (SFRIFG1&OFIFG);                   // Test oscillator fault flag

  P1DIR |= BIT0;						    // P1.0 Output
  P1OUT &= ~BIT0;                           // Clear LED to start

  // Configure RTC_B
  RTCCTL01 |= RTCRDYIE + RTCBCD + RTCHOLD;  // BCD mode, RTC hold, enable RTC read ready interrupt

  RTCYEAR = 0x2011;                         // Year = 0x2011
  RTCMON = 0x10;                            // Month = 0x10 = October
  RTCDAY = 0x07;                            // Day = 0x07 = 7th
  RTCDOW = 0x05;                            // Day of week = 0x05 = Friday
  RTCHOUR = 0x11;                           // Hour = 0x11
  RTCMIN = 0x59;                            // Minute = 0x59
  RTCSEC = 0x45;                            // Seconds = 0x45

  RTCCTL01 &= ~(RTCHOLD);                   // Start RTC calendar mode

  __bis_SR_register(LPM3_bits + GIE);       // Enter LPM3 mode with interrupts
                                            // enabled
  __no_operation();

}

#pragma vector=RTC_VECTOR
__interrupt void RTCISR (void)
{

 while(BAKCTL & LOCKBAK)                    // Unlock backup system
        BAKCTL &= ~(LOCKBAK); 

  switch(__even_in_range(RTCIV,14))
  {
  case  0: break;                           // Vector  0:  No interrupt
  case  2:                                  // Vector  2:  RTCRDYIFG
    P1OUT ^= BIT0;                          // Toggle LED every second
    Seconds = RTCSEC;                       // Read all associated time registers
    Minutes = RTCMIN;
    Hours = RTCHOUR;
    break;
  case  4: break;                           // Vector  4:  RTCEVIFG
  case  6: break;                           // Vector  6:  RTCAIFG
  case  8: break;                           // Vector  8:  RT0PSIFG
  case 10: break;                           // Vector 10:  RT1PSIFG
  case 12: break;                           // Vector 12:  RTCOFIFG
  case 14: break;                           // Vector 14:  Reserved
  default: break;
  }
}
