;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; 
; Filename:	    bsp.asm
;
; Description:  A board support package for the operating system.
;
; Notes:        This code is specific to the Intel 80188 processor.
;
; 
; Copyright (c) 1998 by Michael Barr.  This software is placed into
; the public domain and may be used for any purpose.  However, this
; notice must not be changed or removed and no warranty is either
; expressed or implied by its publication or distribution.
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


    NAME    bsp

    PUBLIC  _contextInit
    PUBLIC  _contextSwitch
    PUBLIC  _idle


bsp SEGMENT WORD PUBLIC 'CODE'

    ASSUME  cs:bsp
    ASSUME  ds:nothing
    ASSUME  es:nothing
    ASSUME  ss:nothing


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; 
; Function:     contextInit()
;
; Description:  Initialize the context of a new task.
;
; Parameters:   pContext  - A pointer to the task's context.
;               pFunc     - A pointer to the task startup routine.
;               pTask     - A pointer to the task object.
;               pStackTop - A pointer to the top of the task's stack.
;
; Notes:
;
; Returns:      None defined.
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
EVEN
_contextInit   PROC    FAR

    push    bp	
    mov     bp, sp

    les     di, dword ptr ss:[bp+6]     ; Get pContext from the caller.

    ;
    ; Initialize the return address.
    ;
    push    ds
    lds     bx, dword ptr ss:[bp+10]    ; Get pFunc from the caller.
    mov     dx, ds
    mov     es:[di], bx             
    mov     es:[di+2], dx

    ;
    ; Initialize the processor flags.
    ;
    pushf							
    pop     ax
    or      ax, 0000001000000000b       ; Enable interrupts by default.
    mov     es:[di+4], ax           

    ;
    ; Initialize the stack segment.
    ;
    les     di, dword ptr ss:[bp+18]    ; Point to the task's stack.
    lds     bx, dword ptr ss:[bp+14]    ; Get pTask from the caller.
    mov     dx, ds
    mov     es:[di-4], bx               ; Place pTask onto the stack.
    mov     es:[di-2], dx

    les     di, dword ptr ss:[bp+6]     ; Point to the task's context.
    lds     bx, dword ptr ss:[bp+18]    ; Get pStack from the caller.
    mov     dx, ds
    sub     bx, 8                       ; Save stack space for pTask.
    mov     es:[di+6], bx           
    mov     es:[di+8], dx

    ;
    ; Initialize the data segment.
    ;
    pop     ds
    mov     dx, ds                  
    mov     es:[di+10], si
    mov     es:[di+12], dx

    pop     bp
    ret

_contextInit   ENDP


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; 
; Function:    contextSwitch()
;
; Description: This is the heart of the operating system.
;
; Parameters:  pOldContext - A pointer to the current context.
;              pNewContext - A pointer to the new context.
;
; Notes:
;
; Returns:     None defined.
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
EVEN
_contextSwitch   PROC    FAR

    push    bp
    mov     bp, sp

    ;
    ; Get pOldContext from the stack.
    ;
    les     di, dword ptr ss:[bp+6] 
    mov     dx, es
    mov     ax, di

    ;
    ; if (pOldContext == NULL) goto fromIdle;
    ;
    or      ax, dx
    jz      fromIdle

    ;
    ; Save the address of the end of this routine.
    ;
    mov     dx, cs
    lea     ax, switchComplete
    mov     es:[di], ax
    mov     es:[di+2], dx

    ;
    ; Save the processor flags.
    ;
    pushf               
    pop     es:[di+4]

    ;
    ; Save the stack segment.
    ;
    mov     dx, ss
    mov     es:[di+6], sp
    mov     es:[di+8], dx

    ;
    ; Save the data segment.
    ;
    mov     dx, ds
    mov     es:[di+10], si
    mov     es:[di+12], dx

fromIdle:        
    ;
    ; Get pNewContext from the stack.
    ;
    les     di, dword ptr ss:[bp+10]
    mov     dx, es
    mov     ax, di

    ;
    ; Restore the data segment.
    ;
    lds     si, dword ptr [di+10]

    ;
    ; Restore the stack segment.
    ;
    mov     dx, es:[di+8]  
    mov     ax, es:[di+6]
    pushf                           ; Save the current interrupt state.
    pop     cx
    cli                             ; Disable interrupts.
    mov     ss, dx
    mov     sp, ax
    push    cx
    popf                            ; Restore the saved interrupt state.

    ;
    ; Restore the processor flags.
    ;
    push    es:[di+4]

    ;
    ; Restore the return address.
    ;
    push    es:[di+2]
    push    es:[di]

    ;
    ; Now return, taking the saved flags with us.
    ;
    iret

switchComplete:            
    pop     bp
    ret

_contextSwitch   ENDP


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; 
; Function:    idle()
;
; Parameters:  pTask - A pointer to the task object.
;
; Description: The idle task.
;
; Notes:
;
; Returns:     None defined.
;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
EVEN
_idle            PROC FAR

idleLoop:
    jmp     idleLoop 

_idle            ENDP


bsp ENDS

END
