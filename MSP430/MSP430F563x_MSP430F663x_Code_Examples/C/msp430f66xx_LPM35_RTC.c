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
//   MSP430F66x Demo - RTC_B, LPM3.5, & alarm
//
//   Description: The RTC_B module is used to set the time, start RTC operation,
//   and read the time from the respective RTC registers. Software will set the
//   original time to 11:59:45 am on Friday October 7, 2011. Then the RTC will
//   be activated through software, and an alarm will be created for the next 
//   minute (12:00:00 pm). The device will then enter LPM3.5 awaiting
//   the event interrupt. Upon being woken up by the event, the LED on the board
//   will be set. 
//
//   NOTE: This code example was created and tested using the MSP-TS430PZ100USB
//   Rev1.2 board. To ensure that LPM3.5 is entered properly, upon opening the 
//   debug window either hit the "Run Free" button on the dropdown menu next to 
//   the run button if you are using CCS, or make sure to check that the 
//   "Release JTAG on GO" option is selected if you are using IAR. 
//   ~1.13uA should be measured on JP1. 
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
//   November 2012
//   Built with IAR Embedded Workbench V5.51.1 & Code Composer Studio V5.2.1
//******************************************************************************

#include <msp430.h>

void Board_Init(void);
void RTC_Init(void);
void EnterLPM35(void);
void WakeUpLPM35(void);

int main (void)
{
  WDTCTL = WDTPW + WDTHOLD;                 // Stop WDT
   
  if(SYSRSTIV == 0x08) 
  {
    // When woken up from LPM3.5, reinit
    WakeUpLPM35();
    // If woken up at noon, set LED
    if((RTCHOUR == 0x12)&&(RTCMIN == 0x00)&&(RTCSEC == 0x00))
    {
      while(1)
        P1OUT |= BIT0;
    }
    // If values do not match, blink LED fast
    else
    {
      while(1)                                  // continuous loop
      {
        P1OUT ^= BIT0;                          // XOR P1.0
        __delay_cycles(20000);                  // Delay
      }
    }
  }
  
  // Init board & RTC, then enter LPM3.5
  Board_Init();
  RTC_Init();
  EnterLPM35();
  
  // Code should not get here
  // Blink LED slowly if LPM3.5 not entered properly
  WakeUpLPM35();
  while(1)
  {
    P1OUT ^= BIT0;                          // XOR P1.0
    __delay_cycles(200000);                 // Delay
  }
}

#pragma vector=RTC_VECTOR
__interrupt void RTCISR (void)
{
  PMMCTL0_H = PMMPW_H;                      // open PMM
  PM5CTL0 &= ~LOCKIO;                       // Clear LOCKBAK and enable ports
  PMMCTL0_H = 0x00;                         // close PMM

  RTCCTL0 &= ~RTCTEVIFG;
  __bic_SR_register_on_exit(LPM4_bits);     // Exit LPM4.5
  __no_operation();
}

void Board_Init(void)
{
  // Port Configuration
  P1OUT = 0x00;P2OUT = 0x00;P3OUT = 0x00;P4OUT = 0x00;P5OUT = 0x00;P6OUT = 0x00;
  P7OUT = 0x00;P8OUT = 0x00;P9OUT = 0x00;PJOUT = 0x00;
  P1DIR = 0xFF;P2DIR = 0xFF;P3DIR = 0xFF;P4DIR = 0xFF;P5DIR = 0xFF;P6DIR = 0xFF;
  P7DIR = 0xFF;P8DIR = 0xFF;P9DIR = 0xFF;PJDIR = 0xFF;

  // Disable VUSB LDO and SLDO
  USBKEYPID   =     0x9628;                 // set USB KEYandPID to 0x9628 
                                            // enable access to USB config reg
  USBPWRCTL &= ~(SLDOEN+VUSBEN);            // Disable the VUSB LDO and the SLDO
  USBKEYPID   =    0x9600;                  // disable access to USB config reg
  __delay_cycles(10000);                    // settle FLL
}

void RTC_Init(void)
{
  // Setup Clock
  UCSCTL6 &= ~(XT1OFF);                     // XT1 On
  UCSCTL6 |= XCAP_3;                        // Internal load cap
  while(BAKCTL & LOCKBAK)                   // Unlock XT1 pins for operation
     BAKCTL &= ~(LOCKBAK);  
  do
  {
    UCSCTL7 &= ~(XT2OFFG + XT1LFOFFG + DCOFFG);
                                            // Clear XT2,XT1,DCO fault flags
    SFRIFG1 &= ~OFIFG;                      // Clear fault flags
  }while (SFRIFG1&OFIFG);                   // Test oscillator fault flag

  // Configure RTC_B
  RTCCTL01 |= RTCTEVIE + RTCBCD + RTCHOLD;  // BCD mode, RTC hold, enable RTC 
                                            // event interrupt
  RTCYEAR = 0x2011;                         // Year = 0x2011
  RTCMON = 0x10;                            // Month = 0x10 = October
  RTCDAY = 0x07;                            // Day = 0x07 = 7th
  RTCDOW = 0x05;                            // Day of week = 0x05 = Friday
  RTCHOUR = 0x11;                           // Hour = 0x11
  RTCMIN = 0x59;                            // Minute = 0x59
  RTCSEC = 0x45;                            // Seconds = 0x45
  
  RTCCTL1 |= RTCTEV_0;                      // Set RTCTEV for 1 minute alarm
  RTCCTL01 &= ~(RTCHOLD);                   // Start RTC calendar mode
  
  // Turn off Clock for LPM4.5 operation
  UCSCTL6 |= XT1OFF;                        // XT1 Off
}

void EnterLPM35(void)
{
  PMMCTL0_H = PMMPW_H;                      // Open PMM Registers for write
  PMMCTL0_L |= PMMREGOFF;                   // and set PMMREGOFF
  __bis_SR_register(LPM4_bits+ GIE);             // Enter LPM3.5 mode with interrupts
  __no_operation();                         // enabled
}

void WakeUpLPM35(void)
{
  PMMCTL0_H = PMMPW_H;                       // open PMM
  PM5CTL0 &= ~LOCKIO;                        // Clear LOCKBAK and enable ports
  PMMCTL0_H = 0x00;                          // close PMM

  // Restore Port settings
  P1OUT = 0x00;P2OUT = 0x00;P3OUT = 0x00;P4OUT = 0x00;P5OUT = 0x00;P6OUT = 0x00;
  P7OUT = 0x00;P8OUT = 0x00;P9OUT = 0x00;PJOUT = 0x00;
  P1DIR = 0xFF;P2DIR = 0xFF;P3DIR = 0xFF;P4DIR = 0xFF;P5DIR = 0xFF;P6DIR = 0xFF;
  P7DIR = 0xFF;P8DIR = 0xFF;P9DIR = 0xFF;PJDIR = 0xFF;

  // Restore Clock so that RTC will be read
  UCSCTL6 &= ~(XT1OFF);                     // XT1 On
  UCSCTL6 |= XCAP_3;                        // Internal load cap
  while(BAKCTL & LOCKBAK)                   // Unlock XT1 pins for operation
     BAKCTL &= ~(LOCKBAK);  
  do
  {
    UCSCTL7 &= ~(XT2OFFG + XT1LFOFFG + DCOFFG);
                                            // Clear XT2,XT1,DCO fault flags
    SFRIFG1 &= ~OFIFG;                      // Clear fault flags
  }while (SFRIFG1&OFIFG);                   // Test oscillator fault flag  
  
  // Reconfig/start RTC
  RTCCTL01 |= RTCBCD + RTCHOLD;
  RTCCTL01 &= ~RTCHOLD;
}
