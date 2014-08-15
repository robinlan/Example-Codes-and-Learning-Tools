/**********************************************************************
 *
 * Filename:    led.cpp
 * 
 * Description: A memory-mapped PCB version of the LED functions.
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

#include "i8018xEB.h"


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
    gProcessor.pPCB->ioPort[1].latch ^= ledMask;

}   /* toggleLed() */

