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
;  MSP430F66xx Demo - ADC12_A, Using the Internal Reference
;
;  Description: This example shows how to use the shared reference for ADC12
;  sampling and performs a single conversion on channel A0. The conversion 
;  results are stored in ADC12MEM0. Test by applying a voltage to channel A0, 
;  then setting and running to a break point at the nop  
;  instruction. To view the conversion results, open an ADC12 register window 
;  in debugger and view the contents of ADC12MEM0
;
;                MSP430F6638
;             -----------------
;         /|\|                 |
;          | |                 |
;          --|RST              |
;            |                 |
;     Vin -->|P6.0/A0          |
;            |                 |

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
            bis.b   #0x01,&P6SEL            ; Enable A/D channel A0
			            
            mov.w 	#REFMSTR + REFVSEL_2 + REFON + REFTCOFF, &REFCTL0
       																			; Initialize REF module 
																						; Enable 2.5V shared reference, disable temperature sensor to save power								; 									
            mov.w	#ADC12ON + ADC12SHT02, &ADC12CTL0
            																; Turn on ADC12, set sampling time  
            mov.w   #ADC12SHP,&ADC12CTL1    ; Use sampling timer
            mov.b   #ADC12SREF_1,&ADC12MCTL0; Vr+=Vref+ and Vr-=AVss
            
            mov.w   #75,R4                  ; Initialize delay loop counter
delay_loop  dec.w   R4                      ; Delay for reference startup
            jne     delay_loop               
            
            bis.w   #ADC12ENC,&ADC12CTL0    ; Enable conversions
while_loop  bis.w   #ADC12SC,&ADC12CTL0     ; Start conversion
poll_ifg    bit.w   #BIT0,&ADC12IFG
            jnc     poll_ifg
            nop                             ; SET BREAKPOINT HERE
            jmp     while_loop 
            
;-------------------------------------------------------------------------------
                  ; Interrupt Vectors
;-------------------------------------------------------------------------------
            .sect   ".reset"                ; POR, ext. Reset
            .short  RESET
            .end
