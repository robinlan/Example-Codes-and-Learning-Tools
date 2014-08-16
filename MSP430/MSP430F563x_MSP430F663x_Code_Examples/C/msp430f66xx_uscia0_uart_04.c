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
//   MSP430F66x Demo - USCI_A0, 9600 UART, SMCLK, LPM0, Echo with over-sampling
//
//   Description: Echo a received character, RX ISR used. Normal mode is LPM0.
//   USCI_A0 RX interrupt triggers TX Echo.
//   If UCOS16=1, UCBRx=Fbrclk/(16*Baudrate)
//   Baud rate divider with UCBRx = 1MHz/(16*9600) = ~6.8
//   ACLK = REFO = ~32768Hz, MCLK = SMCLK = default DCO = 32 x ACLK = 1048576Hz
//   See User Guide for baud rate divider table
//
//   Note that UCA0RXD and UCA0TXD need to be assigned pins via the PMAP 
//  controller.
//
//               MSP430F66x
//             -----------------
//         /|\|                 |
//          | |                 |
//          --|RST              |
//            |                 |
//            |     P2.0/UCA0TXD|------------>
//            |                 | 9600 - 8N1
//            |     P2.1/UCA0RXD|<------------
//
//   Priya Thanigai
//   Texas Instruments Inc.
//   Nov 2009
//   Built with IAR Embedded Workbench Version: 4.20 & Code Composer Studio V4.0
//******************************************************************************
#include <msp430.h>

void Port_Mapping(void);

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
  
  Port_Mapping();
  
  P2SEL |= 0x03;                            // Assign P2.0 to UCA0TXD and...
  P2DIR |= 0x03;                            // P2.1 to UCA0RXD
  
  UCA0CTL1 |= UCSWRST;                      // **Put state machine in reset**
  UCA0CTL1 |= UCSSEL_2;                     // SMCLK
  UCA0BR0 = 6;                              // 1MHz 9600 (see User's Guide)
  UCA0BR1 = 0;                              // 1MHz 9600
  UCA0MCTL = UCBRS_0 + UCBRF_13 + UCOS16;   // Modln UCBRSx=0, UCBRFx=0,
                                            // over sampling
  UCA0CTL1 &= ~UCSWRST;                     // **Initialize USCI state machine**
  UCA0IE |= UCRXIE;                         // Enable USCI_A0 RX interrupt

  __bis_SR_register(LPM0_bits + GIE);       // Enter LPM0, interrupts enabled
  __no_operation();                         // For debugger
}

// Echo back RXed character, confirm TX buffer is ready first
#pragma vector=USCI_A0_VECTOR
__interrupt void USCI_A0_ISR(void)
{
  switch(__even_in_range(UCA0IV,4))
  {
  case 0:break;                             // Vector 0 - no interrupt
  case 2:                                   // Vector 2 - RXIFG
    while (!(UCA0IFG&UCTXIFG));             // USCI_A0 TX buffer ready?
    UCA0TXBUF = UCA0RXBUF;                  // TX -> RXed character
    break;
  case 4:break;                             // Vector 4 - TXIFG
  default: break;
  }
}

void Port_Mapping(void)
{
  // Disable Interrupts before altering Port Mapping registers
  __disable_interrupt();     
  // Enable Write-access to modify port mapping registers
  PMAPPWD = 0x02D52;                        
  
  #ifdef PORT_MAP_RECFG                    
  // Allow reconfiguration during runtime
  PMAPCTL = PMAPRECFG;                     
  #endif  
  
  P2MAP0 = PM_UCA0TXD;
  P2MAP1 = PM_UCA0RXD;

  // Disable Write-Access to modify port mapping registers
  PMAPPWD = 0;                              
  #ifdef PORT_MAP_EINT
  __enable_interrupt();                     // Re-enable all interrupts
  #endif  
}
