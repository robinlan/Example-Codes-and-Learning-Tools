/**********************************************************************
 *
 * Filename:    i8018xEB.cpp
 * 
 * Description: Implementation of the Intel 8018xEB processor class.
 *
 * Notes:       Some of the constants in this file are specific to 
 *              Arcom's Target188EB hardware.
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#include <dos.h>

#include "i8018xEB.h"


#define RELREG 0xFFA8


i8018xEB gProcessor;


/*
 * Static Pointer Initialization
 */
volatile PeripheralControlBlock * 
         i8018xEB::pPCB = (PeripheralControlBlock *)  0x72000000;

unsigned long * 
         i8018xEB::intVectorTable = (unsigned long *) 0x00000000;


/**********************************************************************
 *
 * Function:    i8018xEB()
 * 
 * Description: Constructor for the Intel 8018xEB processor class.
 *
 * Notes:       This should only be invoked once (by limiting the 
 *              number of processor instances to one).
 *
 * Returns:     None defined.
 *
 **********************************************************************/
i8018xEB::i8018xEB()
{
    // Relocate the PCB to memory space address 0x72000h
    outport(RELREG, 0x1720);

}   /* i8018xEB() */


/**********************************************************************
 *
 * Function:    installHandler()
 * 
 * Description: Install an interrupt handler in the Interrupt Vector
 *              Table of the processor.
 *
 * Notes:       
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void
i8018xEB::installHandler(unsigned char nInterrupt, void interrupt (*handler)())
{
    i8018xEB::intVectorTable[nInterrupt] = (unsigned long) handler;
    
}   /* installHandler() */
