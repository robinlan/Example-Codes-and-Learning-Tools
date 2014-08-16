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
;   MSP430F66x Demo - CRC16, fed by DMA, compare w/ software algorithm
;
;   Description: DMA0 is used to transfer a block of 16 16-bit values word-
;   by-word as a repeating block to CRCDIRB. The CRC16 output is then saved,
;   and a similar operation is done to feed an array, that will then be run 
;   through the software loop. The outputs of both methods are then compared 
;   to ensure that the operation of the CRC module is consistent with the 
;   expected outcome. If the values of each output are equal, set P1.0, else 
;   reset.
;
;  ** RAM location 0x3400 - 0x341F used - make sure no compiler conflict **
;   ACLK = 32.768kHz, MCLK = SMCLK = default DCO~1MHz
;  Use large memory model
;
;                MSP430F66xx
;             -----------------
;         /|\|                 |
;          | |                 |
;          --|RST              |
;            |                 |
;            |             P1.0|--> LED
;
;   Tyler Witt
;   Texas Instruments Inc.
;   October 2011
;   Built with Code Composer Studio V4.2
;******************************************************************************
 .cdecls C,LIST,"msp430.h"

CRC_Init    .word   0xFFFF
CRC_Input   .set    0x4400 ; Randomly chosen RAM address
i           .set    R5
CRC_Results .set    R6
SW_Results  .set    R7
CRC_New     .set    R8
LowByte     .set    R9
UpByte      .set    R10
;------------------------------------------------------------------------------
            .global _main
            .text
;------------------------------------------------------------------------------
_main
RESET       mov.w   #0x63FE,SP
            mov.w   #WDTPW+WDTHOLD,&WDTCTL
            bis.b   #0x01,&P1DIR
            bic.b   #0x01,&P1OUT            ; Clear LED to start
            mov.w   CRC_Init,&CRCINIRES     ; Init CRC with 0xFFFF
            ; Do a block transfer to CRC input
            movx.a  #0x3400,&DMA0SA         ; Start block address
            movx.a  #CRCDIRB,&DMA0DA        ; Destination block address
            mov.w   #0010h,&DMA0SZ          ; Block size
            mov.w   #DMADT_5+DMASRCINCR_3+DMADSTINCR_0,&DMA0CTL ; Rpt, inc 
            bis.w   #DMAEN,&DMA0CTL         ; Enable DMA0
            bis.w   #DMAREQ,&DMA0CTL        ; Trigger block transfer
            nop
            mov.w   CRCINIRES,CRC_Results   ; Save results
            ; Do a block transfer to an array for sofware algorithm
            movx.a  #0x3400,&DMA0SA         ; Start block address
            movx.a  #CRC_Input,&DMA0DA         ; Destination block address
            mov.w   #0010h,&DMA0SZ          ; Block size
            mov.w   #DMADT_5+DMASRCINCR_3+DMADSTINCR_3,&DMA0CTL ; Rpt, inc 
            bis.w   #DMAEN,&DMA0CTL         ; Enable DMA0
            bis.w   #DMAREQ,&DMA0CTL        ; Trigger block transfer
            nop

            mov.w   #0x10,i                 ; load index to 16
            mov.w   #CRC_Input,R11           ; Start of input array
Software    mov.w   @R11,LowByte
            bic.w   #0xFF00,LowByte         ; Clear first 8-bits for just lower byte
            mov.w   @R11,UpByte
            swpb    UpByte                  ; swap bytes and clear first 8-bits for
            bic.w   #0xFF00,UpByte          ; just upper byte
            incd.w  R11                     ; Advance pointer in array
            cmp.w   #0x10,i
            jne     Reinit                  ; if not first run, reinit
            mov.w   CRC_Init,R12            ; First run, init with 0xFFFF
            mov.w   LowByte,R13             ; Input first byte
            calla   #CCITT_Update
            jmp     Upper
Reinit      mov.w   CRC_New,R12             ; Init with previous result
            mov.w   LowByte,R13             ; Input next byte
            calla   #CCITT_Update
Upper       mov.w   CRC_New,R12             ; Init with previous result
            mov.w   UpByte,R13              ; Input next byte
            calla   #CCITT_Update
            dec.w   i                       ; Decrement index
            jnz     Software                ; If more data, then input again
            mov.w   CRC_New,SW_Results      ; Save results
            
Mainloop    cmp.w   CRC_Results,SW_Results  ; Was CRC successful?
            jeq     Success                 ; Yes, jump to success
            jmp     Mainloop                ; No, then don't turn on LED
Success     bis.b   #0x01,&P1OUT            ; Success - turn on LED
            jmp     Mainloop                ; Again
            nop                             ; for debug
                                            ;
;-------------------------------------------------------------------------------
CCITT_Update ; Software algorithm - CCITT CRC16 code
;-------------------------------------------------------------------------------
             swpb   R12
             mov.w  R12,CRC_New
             xor.w  R13,CRC_New
             mov.w  CRC_New,R12
             bic.w  #0xFF00,R12
             rpt    #4
             rra    R12
             xor.w  R12,CRC_New
             mov.w  CRC_New,R12
             rpt    #12
             rla    R12
             xor.w  R12,CRC_New
             mov.w  CRC_New,R12
             bic.w  #0xFF00,R12
             rpt    #5
             rla    R12
             xor.w  R12,CRC_New             
             reta
             nop
;------------------------------------------------------------------------------
;           Interrupt Vectors
;------------------------------------------------------------------------------
             .sect   ".reset"
             .short  RESET
             .end
