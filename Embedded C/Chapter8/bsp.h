/**********************************************************************
 *
 * Filename: 	bsp.h
 * 
 * Description: Hardware and project-specific operating system details.
 *
 * Notes:       
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#ifndef _BSP_H
#define _BSP_H


struct Context
{
    int     IP;
    int     CS;
    int     Flags;    
    int     SP;
    int     SS;
    int     SI;
    int     DS;
        
};


#include "task.h"

#define enterCS()   asm { pushf; cli }
#define exitCS()    asm { popf }


extern "C" 
{
  void  contextInit(Context *, void (*run)(Task *), Task *, int * pStackTop);
  void  contextSwitch(Context * pOldContext, Context * pNewContext);
  void  idle();
};


#endif	/* _BSP_H */
