;------------------------------------------------------------------------------------------------
; File: bcdutils.S - binary to bcd conversion routines for msp430-gcc
; Desc: sample code to illustrate how to use asm with Energia
;
; Author: rick@kimballsoftware.com
; Date: 02-21-2013
; Version: 1.0
;------------------------------------------------------------------------------------------------
;
	.file	"bcdutils.S"
	.cpu 430
	.mpy none

;------------------------------------------------------------------------------------------------
;
    .section  .text.u16tobcd,"ax",@progbits
	.p2align 1,0
.global	u16tobcd
	.type	u16tobcd,@function
/*
 * long u16tobcd(unsigned u16)
 *
 * u16 passed via r15
 *
 * convert binary value to bcd value using dadd instruction
 * 1234 decimal converts to 0x1234 bcd
 *
 * result returned via r14:r15 (5 useful digits)
 *
 * Resources used:
 *    REG: 4,0 scratch pad (r15,r14,r13,r12), other (none)
 *    RAM: 0 bytes
 *  FLASH: 20 bytes
 *  STACK: 0 bytes
 * CYCLES: 154 ~9.6usec @ 16MHz
 */
u16tobcd:               ; cycles
                        ;   description
    mov.w   #16,r12     ; 2 set # of bits to convert
    clr.w   r13         ; 1 clear hi
    clr.w   r14         ; 1 clear lo
1:
    rla.w   r15         ; 3 shift msb of u16 to set carry
    dadd.w  r14,r14     ; 1 decimal add low word + carry bit
    dadd.w  r13,r13     ; 1 decimal add hi word + carry bit
    dec.w   r12         ; 2 decrement bit counter
    jnz 1b              ; 2 loop until bit counter is zero
    mov.w   r13,r15     ; 1 results in r14:r15 lo:hi (stardard long gcc ABI)
    ret                 ; 5
.Lfe1:
	.size	u16tobcd,.Lfe1-u16tobcd
;; End of function 

;------------------------------------------------------------------------------------------------
;
    .section  .text.u32tobcd,"ax",@progbits
    .p2align 1,0
.global u32tobcd
    .type   u32tobcd,@function
/*
 * long long u32tobcd(unsigned long u32)
 *
 * u32 passed via r14:r15 (lo:hi)
 *
 * convert binary value to bcd value using dadd instruction
 * 1234567890 decimal converts to 0x1234567890 bcd
 *
 * result returned via r12:r13:r14:r14 long long (10 digits) 40 bits used
 *
 * Resources used:
 *    REG: 4,2 scratch pad (r15,r14,r13,r12), other (R11,R10)
 *    RAM: 0 bytes
 *  FLASH: 36 bytes
 *  STACK: 2 bytes
 * CYCLES: 374 cycles ~23.4usec @ 16MHz
 */
u32tobcd:               ; cycles
                        ;   description
    push r11            ; 3 get 2 scratch registers
    push r10            ; 3
    clr.w   r11         ; 1 clear h lo
    clr.w   r13         ; 1 clear l hi
    clr.w   r12         ; 1 clear l lo
    mov.w   #32,r10     ; 2 set # of bits to convert
1:
    rla.w   r14         ; 4 shift msb of l(u32) into carry bit
    rlc.w   r15         ; 1 shift msb of h(u32) chaining r14
    dadd.w  r12,r12     ; 1 decimal add l#lo word + carry bit
    dadd.w  r13,r13     ; 1 decimal add l#hi word + carry bit
    dadd.w  r11,r11     ; 1 decimal add h#lo word + carry bit
    dec.w   r10         ; 1 decrement bit counter
    jnz 1b              ; 2 loop until bit counter is zero
    mov.w   r11,r14     ; 1 results in r12:r13,r14:r15 l#(lo:hi),h#(lo:hi)
    clr.w   r15         ; 1 h hi ( we only use 40 bits )
    pop r10             ; 2 revert back to
    pop r11             ; 2 original r10,r11
    ret                 ; 5
.Lfe2:
    .size   u32tobcd,.Lfe2-u32tobcd
;; End of function
