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
//   MSP430F66x Demo - USCI_A0, SPI 3-Wire Master Incremented Data
//
//   Description: SPI master talks to SPI slave using 3-wire mode. Incrementing
//   data is sent by the master starting at 0x01. Received data is expected to
//   be same as the previous transmission.  USCI RX ISR is used to handle
//   communication with the CPU, normally in LPM0. If high, P1.0 indicates
//   valid data reception.  Because all execution after LPM0 is in ISRs,
//   initialization waits for DCO to stabilize against ACLK.
//   
//   ACLK = ~32.768kHz, MCLK = SMCLK = DCO ~ 1048kHz.  BRCLK = SMCLK/2
//
//   Use with SPI Slave Data Echo code example. The master code example uses
//   a GPIO P1.1 to reset the SPI slave state machine.
//
//   Note that UCB0CLK, UCB0SIMO and UCB0SOMI need to be assigned pins via the 
//   PMAP controller.
//
//                     MSP430F66x
//                   -----------------
//               /|\|                 |
//                | |                 |
//                --|RST          P1.0|-> LED
//                  |                 |
//                  |             P2.2|-> Data Out (UCA0SIMO)
//                  |                 |
//                  |             P2.1|<- Data In (UCA0SOMI)
//                  |                 |
//Slave SPI reset <-|P1.1         P2.0|-> Serial Clock Out (UCA0CLK)
//
//
//   Priya Thanigai
//   Texas Instruments Inc.
//   Nov 2009
//   Built with IAR Embedded Workbench Version: 4.20 & Code Composer Studio V4.0
//******************************************************************************
#include <msp430.h>

void Port_Mapping(void);

unsigned char MST_Data,SLV_Data;
unsigned char temp;

int main(void)
{
  volatile unsigned int i;

  WDTCTL = WDTPW+WDTHOLD;                   // Stop watchdog timer
  Port_Mapping();
  
  P2SEL |= BIT0+BIT1+BIT2;                  // Assign P2.0 to UCB0CLK and...
  P2DIR |=  BIT0+BIT1+BIT2;                 // P2.1 UCB0SOMI P2.2 UCB0SIMO

  // LED + Chip select
  P1OUT = 0;
  P1DIR = BIT0 + BIT1;
    
  UCA0CTL1 |= UCSWRST;                      // **Put state machine in reset**
  UCA0CTL0 |= UCMST+UCSYNC+UCCKPL+UCMSB;    // 3-pin, 8-bit SPI master
                                            // Clock polarity high, MSB
  UCA0CTL1 |= UCSSEL_2;                     // SMCLK
  UCA0BR0 = 0x02;                           // /2
  UCA0BR1 = 0;                              //
  UCA0MCTL = 0;                             // No modulation
  UCA0CTL1 &= ~UCSWRST;                     // **Initialize USCI state machine**
  UCA0IE = UCRXIE ;                         // Enable USCI_A0 RX,TX interrupt

  P1OUT |= BIT1;                            // chip select for slave

  __delay_cycles(100);                      // Wait for slave to initialize
 
  MST_Data = 0x01;                          // Initialize data values
  SLV_Data = 0x00;                          //

  while (!(UCA0IFG&UCTXIFG));               // USCI_A0 TX buffer ready?
  UCA0TXBUF = MST_Data;                     // Transmit first character

  __bis_SR_register(LPM0_bits + GIE);       // CPU off, enable interrupts
}

#pragma vector=USCI_A0_VECTOR
__interrupt void USCI_A0_ISR(void)
{
  volatile unsigned int i;

  switch(__even_in_range(UCA0IV,4))
  {
    case 0: break;                          // Vector 0 - no interrupt
    case 2:                                 // Vector 2 - RXIFG
      while (!(UCA0IFG&UCTXIFG));           // USCI_A0 TX buffer ready?

      if (UCA0RXBUF==SLV_Data)              // Test for correct character RX'd
        P1OUT |= 0x01;                      // If correct, light LED
      else
        P1OUT &= ~0x01;                     // If incorrect, clear LED

      MST_Data++;                           // Increment data
      SLV_Data++;
      UCA0TXBUF = MST_Data;                 // SET BREAKPOINT HERE! 
      __delay_cycles(500);
      break;
    case 4:                                 // Vector 4 - TXIFG      
      break;                          
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
  
  P2MAP0 = PM_UCA0CLK;
  P2MAP1 = PM_UCA0SOMI;
  P2MAP2 = PM_UCA0SIMO;

  // Disable Write-Access to modify port mapping registers
  PMAPPWD = 0;                              
  #ifdef PORT_MAP_EINT
  __enable_interrupt();                     // Re-enable all interrupts
  #endif  
}
