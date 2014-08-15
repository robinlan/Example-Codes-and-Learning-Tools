/**********************************************************************
 *
 * Filename:    led.c
 * 
 * Description: LED-related functionality.
 *
 * Notes:       The constants in this file are specific to Arcom's 
 *              Target188EB hardware.
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#include "led.h"


/**********************************************************************
 *
 * Function:    toggleLed()
 *
 * Description: Toggle the state of one or both LED's.
 *
 * Notes:       This function is specific to Arcom's Target188EB board.
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void 
toggleLed(unsigned char ledMask)
{
    #define P2LTCH 0xFF5E       /* The address of the I/O register.   */

    asm {
        mov dx, P2LTCH          /* Load the address of the register.  */
        in  al, dx              /* Read the contents of the register. */

        mov ah, ledMask         /* Move the ledMask into a register.  */
        xor al, ah              /* Toggle the requested bits.         */

        out dx, al              /* Write the new register contents.   */
    };

}   /* toggleLed() */
