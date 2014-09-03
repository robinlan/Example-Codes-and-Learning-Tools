//******************************************************************************
//  MSP430F5438 Demo - Real Time Clock Workaround
//
//  Description: This program demonstrates the RTC Workaround for the RTC 
//  Errata deailed in the XMS '5438 Errata under RTC3
//  Do not change the code in the ASM file (.s43) all nops in there are needed
//  ACLK = REFO = 32768Hz, MCLK = SMCLK = default DCO = 32 x ACLK = 1048576Hz
//
//                MSP430F54x
//             -----------------
//         /|\|                 |
//          | |                 |
//          --|RST              |
//
//  W. Goh
//  Texas Instruments Inc.
//  December 2008
//  Built with CCE version 3.2.2 and IAR Embedded Workbench Version: 4.11B
//******************************************************************************

#include <msp430x54x.h>
#include "RTC.h"

void main(void)
{
  WDTCTL = WDTPW + WDTHOLD;                 // Stop Watchdog Timer

  RTCCTL01 |= RTCBCD+RTCHOLD+RTCMODE;       // RTC enable, BCD mode,
                                            // alarm every Minute,
                                            // enable RTC interrupt
  while(1)
  {
    SetRTCYEAR(2008);                       // RTCTIM0 = 0x0F19
    SetRTCMON(6);                           // RTCTIM1 = 0x010E
    SetRTCDOW(1);                           // RTCDATE = 0x0611
    SetRTCDAY(17);                          // RTCYEAR = 0x07D8
    SetRTCHOUR(14);
    SetRTCMIN(15);
    SetRTCSEC(25);

    __no_operation();                       // ** SET BREAKPOINT HERE **
                                            // View RTC Registers to verify
                                            // values has changed correctly

    SetRTCYEAR(0x2008);                     // RTCTIM0 = 0x172D
    SetRTCMON(8);                           // RTCTIM1 = 0x020A
    SetRTCDOW(2);                           // RTCDATE = 0x0814
    SetRTCDAY(20);                          // RTCYEAR = 0x2008
    SetRTCHOUR(10);
    SetRTCMIN(23);
    SetRTCSEC(45);

    __no_operation();                       // ** SET BREAKPOINT HERE **
                                            // View RTC Registers to verify
                                            // values has changed correctly
  }
}
