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
//   MSP430F66x Demo - LPM4.5, Backup RAM
//
//   Description: Board is setup to first init all ports, setup for P1.4 lo/hi
//         transition interrupt, and start XT1 for setting BAKMEM registers.
//         After registers are written, XT1 is disabled, and LPM4.5 is entered.
//         Upon a lo/hi transition to P1.4, the device will awaken from LPM4.5
//         and attempt to verify if the contents remain as they were programmed
//         previous to entering LPM4.5. If so, the LED is lit, if not, the LED 
//         will blink quickly. If the device was not properly put into LPM4.5 
//         (see NOTE) before the transition was made, the LED will blink slowly.
//	
//
//   NOTE: This code example was tested on the MSP-TS430PZ100USB Rev1.2 board.
//         To get proper execution of this code example, first switch the JP3
//         to external power, and force 3.0V on the VCC pin. Remove JP1 and set
//         a multimeter on current reading to ensure proper entrance/exit of 
//         LPM4.5. Now build the code example and program the device via the 
//         Debug button. Press "Run" then "Terminate All" buttons. Detatch the 
//         JTAG interface with the FET from the board. You should read around
//         0.38uA current through JP1. Attatch a jumper wire from P1.4 (Pin 38)
//         to Vcc, to trigger a lo/hi transition. The LED on the board should 
//         light up, signifying a successful verificaiton of the programmed
//         values for all four BAKMEM registers.
//
//  //* An external watch crystal on XIN XOUT is required for ACLK *//	
//   ACLK = 32.768kHz, MCLK = SMCLK = default DCO~1MHz
//
//                MSP430F66xx
//             -----------------
//         /|\|              XIN|-
//          | |                 | 32kHz
//          --|RST          XOUT|-
//      /|\   |                 |
//       --o--|P1.4         P1.0|-->LED
//      \|/
//
//   Tyler Witt
//   Texas Instruments Inc.
//   October 2011
//   Built with IAR Embedded Workbench V6.0 & Code Composer Studio V4.2
//******************************************************************************

#include <msp430.h>

void Board_Init(void);
void EnterLPM45(void);
void WakeUpLPM45(void);
void SetBAKMEM(void);

int main (void)
{
  WDTCTL = WDTPW + WDTHOLD;                 // Stop WDT
  
  if(SYSRSTIV == 0x08)
  {
  	WakeUpLPM45();
    P1DIR |= BIT0;                          // P1.0 Output
    // read backup RAM. if values match, turn on LED. else, clear LED.
    if((BAKMEM0==0x0000)&&(BAKMEM1==0x1111)&&(BAKMEM2==0x2222)&&(BAKMEM3==0x3333))
    {
      while(1)
        P1OUT |= BIT0;                      // all values match, turn on LED
    }
    else
    {
      while(1)                              // Blink LED fast if values don't 
      {                                     // all match
        P1OUT ^= BIT0;
        __delay_cycles(20000);
      }
    }
  }
  
  Board_Init();
  SetBAKMEM();
  EnterLPM45();

  P1DIR |= BIT0;                            // Code should NOT go here
  while(1)                                  // Blink LED slow if not an LPM4.5
  {                                         // Wakeup condition
    P1OUT ^= BIT0;
    __delay_cycles(500000);
  }

}

#pragma vector=PORT1_VECTOR
__interrupt void Port1_ISR (void)
{
  PMMCTL0_H = PMMPW_H;                      // open PMM
  PM5CTL0 &= ~LOCKIO;                       // Clear LOCKBAK and enable ports
  PMMCTL0_H = 0x00;                         // close PMM

  P1IFG &= ~BIT4;                           // Clear P1.4 IFG
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

  // P1.4 Interrupt Configuration
  P1IES &= ~BIT4;                           // P1.4 Lo/Hi edge
  P1IE = BIT4;                              // P1.4 interrupt enabled
  P1IFG = 0;                           		// P1.4 IFG cleared

  // Disable VUSB LDO and SLDO
  USBKEYPID   =     0x9628;                 // set USB KEYandPID to 0x9628 
                                            // enable access to USB config reg
  USBPWRCTL &= ~(SLDOEN+VUSBEN);            // Disable the VUSB LDO and the SLDO
  USBKEYPID   =    0x9600;                  // disable access to USB config reg
  __delay_cycles(10000);                    // settle FLL
}

void EnterLPM45(void)
{
  __enable_interrupt();
    // Turn off Clock for LPM4.5 operation
  UCSCTL6 |= XT1OFF;                        // XT1 Off  
  BAKCTL |= BAKDIS;                         // Supply power from Vcc, Vbat disabled
  PMMCTL0_H = PMMPW_H;                      // Open PMM Registers for write and set
  PMMCTL0_L |= PMMREGOFF;                   // PMMREGOFF
  __bis_SR_register(LPM4_bits);             // Enter LPM4.5 mode with interrupts
  __no_operation();                         // enabled
}

void WakeUpLPM45(void)
{
  PMMCTL0_H = PMMPW_H;                      // open PMM
  PM5CTL0 &= ~LOCKIO;                       // Clear LOCKBAK and enable ports
  PMMCTL0_H = 0x00;                         // close PMM

  // Restore Port settings
  P1OUT = 0x00;P2OUT = 0x00;P3OUT = 0x00;P4OUT = 0x00;P5OUT = 0x00;P6OUT = 0x00;
  P7OUT = 0x00;P8OUT = 0x00;P9OUT = 0x00;PJOUT = 0x00;
  P1DIR = 0xFF;P2DIR = 0xFF;P3DIR = 0xFF;P4DIR = 0xFF;P5DIR = 0xFF;P6DIR = 0xFF;
  P7DIR = 0xFF;P8DIR = 0xFF;P9DIR = 0xFF;PJDIR = 0xFF; 
  
  while(BAKCTL & LOCKBAK)                   // Unlock XT1 pins for operation
     BAKCTL &= ~(LOCKBAK);    
}

void SetBAKMEM(void)
{
  // Set backup RAM values
  BAKMEM0 = 0x0000;
  BAKMEM1 = 0x1111;
  BAKMEM2 = 0x2222;
  BAKMEM3 = 0x3333; 
}
