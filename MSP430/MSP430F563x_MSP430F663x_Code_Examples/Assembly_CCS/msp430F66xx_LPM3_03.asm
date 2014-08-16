; --COPYRIGHT--,BSD_EX
;  Copyright (c) 2012, Texas Instruments Incorporated
;  All rights reserved.
; 
;  Redistribution and use in source and binary forms, with or without
;  modification, are permitted provided that the following conditions
;  are met:
; 
;  *  Redistributions of source code must retain the above copyright
;     notice, this list of conditions and the following disclaimer.
; 
;  *  Redistributions in binary form must reproduce the above copyright
;     notice, this list of conditions and the following disclaimer in the
;     documentation and/or other materials provided with the distribution.
; 
;  *  Neither the name of Texas Instruments Incorporated nor the names of
;     its contributors may be used to endorse or promote products derived
;     from this software without specific prior written permission.
; 
;  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
;  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
;  THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
;  PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
;  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
;  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
;  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
;  OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
;  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
;  OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
;  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
; 
; ******************************************************************************
;  
;                        MSP430 CODE EXAMPLE DISCLAIMER
; 
;  MSP430 code examples are self-contained low-level programs that typically
;  demonstrate a single peripheral function or device feature in a highly
;  concise manner. For this the code may rely on the device's power-on default
;  register values and settings such as the clock configuration and care must
;  be taken when combining code from several examples to avoid potential side
;  effects. Also see www.ti.com/grace for a GUI- and www.ti.com/msp430ware
;  for an API functional library-approach to peripheral configuration.
; 
; --/COPYRIGHT--
;******************************************************************************
;   MSP430x66xx Demo - Enters LPM3 with ACLK =VLO;  LFXT1, REF0 disabled, 
;                      VUSB LDO and SLDO disabled, SVS default state
;
;   Description: Configure ACLK = VLO and enter LPM3. Measure current.
;   ACLK = VLO = 12kHz, MCLK = SMCLK = default DCO
; 
;           MSP430F663x
;         ---------------
;     /|\|               |
;      | |               |
;      --|RST            |
;        |               |
;        |               |  
;        |               |
;
;   Priya Thanigai
;   Texas Instruments Inc.
;   August 2011
;   Built with CCS V5
;******************************************************************************
            .cdecls C,LIST,"msp430.h"

;-------------------------------------------------------------------------------
            .global _main
            .text                           ; Assemble to Flash memory
;-------------------------------------------------------------------------------
_main
RESET       mov.w   #0x63FE,SP              ; Initialize stackpointer
            mov.w   #WDTPW + WDTHOLD,&WDTCTL; Stop WDT

            mov.w   #SELA_1,&UCSCTL4

            mov.b   #0xFF,&P1DIR
            mov.b   #0xFF,&P2DIR
            mov.b   #0xFF,&P3DIR
            mov.b   #0xFF,&P4DIR
            mov.b   #0xFF,&P5DIR
            mov.b   #0xFF,&P6DIR
            mov.b   #0xFF,&P7DIR
            mov.b   #0xFF,&P8DIR
            mov.b   #0xFF,&P9DIR
            mov.w   #0xFF,&PJDIR
            clr.b   &P1OUT
            clr.b   &P2OUT
            clr.b   &P3OUT
            clr.b   &P4OUT
            clr.b   &P5OUT
            clr.b   &P6OUT
            clr.b   &P7OUT
            clr.b   &P8OUT
            clr.b   &P9OUT
            clr.w   &PJOUT
            ; disable USB LDOs           
            mov.w   #0x9628,&USBKEYPID
            bic.w   #SLDOEN+VUSBEN,&USBPWRCTL
            mov.w   #0x9600,&USBKEYPID
            
            ; disable SVS
            mov.b   #PMMPW_H,&PMMCTL0_H
            bic.w   #SVMHE+SVSHE,&SVSMHCTL
            bic.w   #SVMLE+SVSLE,&SVSMLCTL
            
            ; blink LED a few times before entering LPM
            mov.w   #0Fh,R8
Blink       dec.w   R8
            jz      Mainloop            
            xor.b   #BIT0,&P1OUT
            
Wait        mov.w   #05000,R15             ; Delay to R15
L1          dec.w   R15                     ; Decrement R15
            jnz     L1                      ; Delay over?
            
            jmp     Blink
           
            clr.b   &P1OUT
Mainloop    bis.w   #LPM3,SR                ; Enter LPM3
            nop                             ; For debugger

;-------------------------------------------------------------------------------
                  ; Interrupt Vectors
;-------------------------------------------------------------------------------
            .sect   ".reset"                ; POR, ext. Reset
            .short  RESET
            .end
