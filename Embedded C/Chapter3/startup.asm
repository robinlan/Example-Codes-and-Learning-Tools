;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; 
; Filename:	    startup.asm
;
; Description:  Startup code for Borland C/C++.
;
; Notes:        This module should be specified first during linking.
;
; Warnings:	    The order of the segments within this file matters.
;
; 
; Copyright (c) 1998 by Michael Barr.  This software is placed into
; the public domain and may be used for any purpose.  However, this
; notice must not be changed or removed and no warranty is either
; expressed or implied by its publication or distribution.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

    NAME    startup

    ;
    ; Global Variables
    ;
	PUBLIC 	_errno				; Required by the Borland libraries.
    PUBLIC  __psp				; Required for dynamic memory allocation.
    PUBLIC  __brklvl			; Required for dynamic memory allocation.
    PUBLIC  __heapbase			; Required for dynamic memory allocation.
    PUBLIC	__heaptop			; Required for dynamic memory allocation.

    ;
    ; Functions Defined Externally
    ;
	EXTRN	_main : FAR

    ;
    ; Constants
    ;
	stack_size EQU 1024			; By default, create a 1-kbyte stack.
	ram_size   EQU 2000h		; The Arcom board has 2000h pages of RAM.

	PCB        EQU 0ff00h		; Base of the Peripheral Control Block.
    GCS0ST     EQU PCB + 80h	; General-purpose chip select 0 start.
    GCS0SP	   EQU PCB + 82h	; General-purpose chip select 0 stop.
    GCS1ST     EQU PCB + 84h	; General-purpose chip select 1 start.
    GCS1SP	   EQU PCB + 86h	; General-purpose chip select 1 stop.

	;
	; Initializer/Terminator Structure Format (used by C++ modules)
	;
	Initializer	STRUC

		ctype	 DB	?		; Code Segment Type (0 = near, 1 = far)
		priority DB	?		; Priority (0 = highest, 0xff = lowest)
		foffset	 DW	?		; Function Pointer (Offset)
		fsegment DW	?		; Function Pointer (Segment)

	Initializer	ENDS


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Segment:	    _text
;
; Description:	The following segment contains the startup code.
;
; Notes:        If desired, this segment can be located in ROM.
;
; Warnings:	    The terminators are not executed, since embedded
;               applications are not expected to complete and exit.
;       		However, an _exit_ segment must still be declared
;               here, for compatibility with existing C++ libraries.
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

_text SEGMENT PARA PUBLIC 'CODE'

	ASSUME	CS:_text

_startup PROC FAR
	;
	; Disable interrupts and set the direction flag.
	;
	cli
	cld

	;
	; Perform hardware initializations that may not have been
    ; done by the hardware initialization code in EEPROM.
    ;
    mov     dx, GCS0ST
    mov     ax, 7000h
    out     dx, ax

    mov     dx, GCS0SP
    mov     ax, 710Ah
    out     dx, ax

    mov     dx, GCS1ST
    mov     ax, 7100h
    out     dx, ax

    mov     dx, GCS1SP
    mov     ax, 720Ah
    out     dx, ax

	;
	; Initialize the segment registers.
	;
	mov		ax, dgroup
	mov		ds, ax
	mov		es, ax
	ASSUME	ds:dgroup, es:dgroup

initData:
	;
	; Zero the uninitialized data segment.
	;
	xor		ax, ax
	mov		di, offset dgroup:bss_start
	mov		cx, offset dgroup:data_end
	sub		cx, di
	shr		cx, 1
	jcxz	initStack
	rep		stosw

initStack:
	;
	; Initialize the stack.
	;
	mov		ax, _stack
	mov		ss, ax
	mov		sp, offset stack_top
	ASSUME	ss:_stack

initHeap:
    ; 
    ; Initialize the heap.
    ;
    mov		ax, _farheap				; heapBase
    inc     ax

    mov		[__brklvl + 2], ax
    mov     [__brklvl], 0000h
    mov		[__heapbase + 2], ax
    mov     [__heapbase], 0000h
    mov     [__psp], ax

    mov		bx, ram_size				; heapEnd
    mov     [__heaptop + 2], bx
    mov     [__heaptop], 0000h

    mov     dx, _farheap
    mov     ds, dx
    ASSUME	ds:_farheap

    mov     [id], 'Z'
    mov		[owner], 0
	sub		bx, ax
    mov     [npages], bx

initModules:
	;
	; Call any static initializers found in the _init_ segment.  
	; The C++ language guarantees that these functions are called
	; before main().  
	;
    ; The following registers are used.
	; 	SI - offset of the next initializer
	; 	DI - offset of the last initializer
	; 	CX - number of initializers remaining
	; 	BX - current initializer
	; 	AH - current priority
	;
	mov		si, offset igroup:init_start
	mov		di, offset igroup:init_end
    mov     ax, dgroup
    mov     ds, ax
	mov		ax, igroup
	mov		es, ax
	ASSUME	ds:dgroup, es:nothing

	mov		ax, di
	sub		ax, si
	sub		dx, dx
	mov		cx, SIZE Initializer
	idiv	cx
	mov		cx, ax					; CX has total number of initializers.
	jcxz	callMain
	sub		ah, ah					; AH has current priority (start at 0).

firstInitializer:
	mov		bx, si					; Restart, from the top of the table.

nextInitializer:
	cmp		bx, di					; Are there initializers left to check?
	jae		nextPriority			; If not, try the next priority.
	cmp		es:[bx.priority], ah	; Check this initializer's priority.
	jne		tryNextInitializer		; If it's not a match, try the next.

	push	es
	push	ax
	push	bx
	push	cx
	push	si
	push	di

	call	dword ptr es:[bx.foffset]	; Call the initializer

	pop		di
	pop		si
	pop		cx
	pop		bx
	pop		ax
	pop		es

	dec		cx							; Decrement number of initializers.
	jz		callMain					; If zero left, continue.

tryNextInitializer:
	add		bx, SIZE Initializer
	jmp		nextInitializer

nextPriority:
	inc		ah					; Process the next priority.
	jmp		firstInitializer

callMain:
	;
	; Enable interrupts and transfer control to C/C++.
	;
	sti
	call	_main

	;
	; A return from main() is unexpected.  Halt the processor.
	;
	hlt

_startup endp

_text ENDS


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Segments:     _init_, _initend_, _exit_, _exitend_
;
; Description:  The following segments are used for C++ initializers
;               and terminators.  
;
; Notes:        If possible, these segments should be located in ROM.
;
; Warnings:	    The terminators are not executed, since embedded
;               applications are not expected to complete and exit.
;       	    However, an _exit_ segment must still be declared
;               here, for compatibility with existing libraries.
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

_init_ SEGMENT PARA PUBLIC 'INITDATA'

	init_start LABEL DWORD

_init_ ENDS

igroup GROUP _init_


_initend_ SEGMENT BYTE PUBLIC 'INITDATA'

	init_end LABEL DWORD

_initend_ ENDS

igroup GROUP _initend_


_exit_ SEGMENT DWORD PUBLIC 'EXITDATA'

	exit_start LABEL DWORD

_exit_ ENDS

igroup GROUP _exit_


_exitend_ SEGMENT BYTE PUBLIC 'EXITDATA'

	exit_end LABEL DWORD

	DB 16 DUP (?)			; Force the next segment to a new paragraph

_exitend_ ENDS

igroup GROUP _exitend_


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Segments:	    _data, _bss, _bssend, _stack, _farheap
;
; Description:  The following segments are used for the initialized 
;               and uninitialized data, stack, and heap, respectively.
;
; Notes:        These segments must be located in RAM.  In fact, the 
;               _farheap segment will contain whatever RAM is left over
;               after the others are allocated space.
;
; Warnings:	    For compatibility with Borland C/C++, the order of
;               these segments must not be changed.
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

_data SEGMENT PARA PUBLIC 'DATA'

	data_start LABEL BYTE		; Mark the start of the initialized data.

_data ENDS

dgroup GROUP _data 


_bss SEGMENT WORD PUBLIC 'BSS'

	bss_start LABEL BYTE		; Mark the start of the uninitialized data.

    _errno     DW ?
    __psp      DW ?
    __brklvl   DW ?
               DW ?
    __heapbase DW ?
			   DW ?
    __heaptop  DW ?
			   DW ?

_bss ENDS

dgroup GROUP _bss 


_bssend SEGMENT WORD PUBLIC 'BSSEND'

	DW ?						; Give a fixed size for proper alignment.
	data_end LABEL WORD			; Mark the end of the uninitialized data.

_bssend	ENDS

dgroup GROUP _bssend


_stack SEGMENT PARA STACK 'STACK'

	DB stack_size DUP (?)	 	; Reserve space for the stack.

	EVEN
    stack_top DW ?

_stack ENDS


_farheap SEGMENT PARA PUBLIC 'FARHEAP'

    id 		DB ?
    owner 	DW ?
    npages 	DW ?

    DB 11 DUP (?)				; Round the segment up to a full page.

_farheap ENDS


END	_startup			; End module and declare _startup as entry point.
