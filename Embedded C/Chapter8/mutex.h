/**********************************************************************
 *
 * Filename:    mutex.h
 * 
 * Description: Header file for the Mutex class.
 *
 * Notes:       
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#ifndef _MUTEX_H
#define _MUTEX_H


#include "bsp.h"


class Mutex
{
    public:

        Mutex();

        void  take(void);
        void  release(void);

     private:

        TaskList  waitingList;

        enum { Available, Held } state;
};


#endif	/* _MUTEX_H */
