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
;   MSP430F66xx Demo - ADC12_A, Sample A0, Set P1.0 if A0 > 0.5*AVcc
;
;   Description: A single sample is made on A0 with reference to AVcc.
;   Software sets ADC12SC to start sample and conversion - ADC12SC
;   automatically cleared at EOC. ADC12 internal oscillator times sample (16x)
;   and conversion. In mainloop  waits in LPM0 to save power until ADC12
;   conversion complete, ADC12_ISR will force exit from LPM0 in Mainloop on
;   reti. If A0 > 0.5*AVcc, P1.0 set, else reset.
;
;                MSP430F6638
;             -----------------
;         /|\|                 |
;          | |                 |
;          --|RST              |
;            |                 |
;     Vin -->|P6.0/A0      P1.0|--> LED
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
            mov.w   #ADC12SHT02 + ADC12ON,&ADC12CTL0  ; Sampling time, ADC12 on
            mov.w   #ADC12SHP,&ADC12CTL1    ; Use sampling timer
            mov.w   #0x01,&ADC12IE          ; Enable interrupt
            bis.w   #ADC12ENC,&ADC12CTL0    
            bis.b   #0x01,&P6SEL            ; P6.0 ADC option select
            bis.b   #0x01,&P1DIR            ; P1.0 output
            
while_loop  bis.w   #ADC12SC,&ADC12CTL0     ; Start sampling/conversion
            bis.w   #LPM0 + GIE,SR          ; LPM0, ADC12_ISR will force exit
            nop                             ; used for debugging
            jmp     while_loop

;-------------------------------------------------------------------------------
ADC12_ISR
;-------------------------------------------------------------------------------
            add.w   &ADC12IV,PC             ; Add offset to PC
            reti                            ; Vector 0:  No interrupt
            reti                            ; Vector 2:  ADC overflow
            reti                            ; Vector 4:  ADC timing overflow
            jmp     ADC12IFG0_HND           ; Vector 6:  ADC12IFG0
            reti                            ; Vector 8:  ADC12IFG1
            reti                            ; Vector 10: ADC12IFG2
            reti                            ; Vector 12: ADC12IFG3
            reti                            ; Vector 14: ADC12IFG4
            reti                            ; Vector 16: ADC12IFG5
            reti                            ; Vector 18: ADC12IFG6
            reti                            ; Vector 20: ADC12IFG7
            reti                            ; Vector 22: ADC12IFG8
            reti                            ; Vector 24: ADC12IFG9
            reti                            ; Vector 26: ADC12IFG10
            reti                            ; Vector 28: ADC12IFG11
            reti                            ; Vector 30: ADC12IFG12
            reti                            ; Vector 32: ADC12IFG13
            reti                            ; Vector 34: ADC12IFG14

ADC12IFG0_HND
            cmp.w   #0x7FF,&ADC12MEM0       ; ADC12MEM = (A0 >= 0.5AVcc)? 
            jnc     less_than  
grtr_or_eql bis.b   #BIT0,&P1OUT            ; yes -> P1.0 = 1  
            jmp     exit_isr                
less_than   bic.b   #BIT0,&P1OUT            ; no -> P1.0 = 0
exit_isr    bic.w   #LPM0,0(SP)             ; Exit active CPU 
            reti    
            
;-------------------------------------------------------------------------------
                  ; Interrupt Vectors
;-------------------------------------------------------------------------------
            .sect   ".int54"                ; ADC12 isr vector
            .short  ADC12_ISR
            .sect   ".reset"                ; POR, ext. Reset
            .short  RESET
            .end
