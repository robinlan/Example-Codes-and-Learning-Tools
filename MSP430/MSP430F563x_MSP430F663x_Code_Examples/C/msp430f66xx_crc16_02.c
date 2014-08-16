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
//   MSP430F66x Demo - CRC16, fed by DMA, compare w/ software algorithm
//
//   Description: DMA0 is used to transfer a block of 16 16-bit values word-
//   by-word as a repeating block to CRCDIRB. The CRC16 output is then saved,
//   and a similar operation is done to feed an array, that will then be run 
//   through the software loop. The outputs of both methods are then compared 
//   to ensure that the operation of the CRC module is consistent with the 
//   expected outcome. If the values of each output are equal, set P1.0, else 
//   reset.
//
//  ** RAM location 0x3400 - 0x341F used - make sure no compiler conflict **
//   ACLK = 32.768kHz, MCLK = SMCLK = default DCO~1MHz
//  Use large memory model
//
//                MSP430F66xx
//             -----------------
//         /|\|                 |
//          | |                 |
//          --|RST              |
//            |                 |
//            |             P1.0|--> LED
//
//   Tyler Witt
//   Texas Instruments Inc.
//   October 2011
//   Built with IAR Embedded Workbench V6.0 & Code Composer Studio V4.2
//******************************************************************************

#include <msp430.h>
unsigned int CRC_Init = 0xFFFF;
unsigned int CRC_Input[16];
unsigned int i;
unsigned int CRC_Results;
unsigned int SW_Results;
unsigned int CRC_New;

// Software Algorithm Function Definition
unsigned int CCITT_Update(unsigned int, unsigned int);

int main (void)
{
  WDTCTL = WDTPW + WDTHOLD;                 // Stop WDT
  P1DIR |= BIT0;						    // P1.0 Output
  P1OUT &= ~BIT0;							// Clear LED to start

  // Init CRC
  CRCINIRES = CRC_Init;                     // Init CRC with 0xFFFF

  // Use DMA to feed CRC
  __data16_write_addr((unsigned short) &DMA0SA,(unsigned long) 0x3400);
                                            // Source block address
  __data16_write_addr((unsigned short) &DMA0DA,(unsigned long) &CRCDIRB);
                                            // Destination single address --> CRCDIRB
  DMA0SZ = 16;                              // Block size
  DMA0CTL = DMADT_5+DMASRCINCR_3+DMADSTINCR_0; // rpt, inc source only
  DMA0CTL |= DMAEN;                         // Enable DMA0
  DMA0CTL |= DMAREQ;						// Trigger block transfer
  __no_operation();
  
  CRC_Results = CRCINIRES;				    // Save results (per CRC-CCITT standard)

  // Use DMA to fill an array to run through the software algorithm
  __data16_write_addr((unsigned short) &DMA0SA,(unsigned long) 0x3400);
                                            // Source block address
  __data16_write_addr((unsigned short) &DMA0DA,(unsigned long) &CRC_Input);
                                            // Destination single address --> CRCDIRB
  DMA0SZ = 16;                              // Block size
  DMA0CTL = DMADT_5+DMASRCINCR_3+DMADSTINCR_3; // rpt, inc
  DMA0CTL |= DMAEN;                         // Enable DMA0
  DMA0CTL |= DMAREQ;						// Trigger block transfer
  __no_operation();
  
  
  for(i=0;i<16;i++)
  {
    // Input values into Software algorithm (requires 8-bit inputs)
    unsigned int LowByte = (CRC_Input[i] & 0x00FF); // Clear upper 8 bits to get lower byte
    unsigned int UpByte = (CRC_Input[i] >> 8); // Shift right 8 bits to get upper byte
    // First input lower byte
    if(i==0)
      CRC_New = CCITT_Update(CRC_Init,LowByte);
    else
      CRC_New = CCITT_Update(CRC_New,LowByte);
    // Then input upper byte
    CRC_New = CCITT_Update(CRC_New,UpByte);
  }
  SW_Results = CRC_New;
  
  // Compare data output results
  if(CRC_Results==SW_Results)               // if data is equal
    P1OUT |= BIT0;							// set the LED
  else
    P1OUT &= ~BIT0;							// if not, clear LED
  
  while(1);                                 // infinite loop
}

// Software algorithm - CCITT CRC16 code
unsigned int CCITT_Update(unsigned int init, unsigned int input)
{
  unsigned int CCITT;
  CCITT =(unsigned char)(init >> 8)|(init << 8);
  CCITT ^= input;
  CCITT ^= (unsigned char)(CCITT & 0xFF) >> 4;
  CCITT ^= (CCITT << 8) << 4;
  CCITT ^= ((CCITT & 0xFF) << 4) << 1;
  return CCITT;
}
