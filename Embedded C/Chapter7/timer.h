/**********************************************************************
 *
 * Filename:    timer.h
 * 
 * Description: Header file for the Intel 8018xEB Timer class.
 *
 * Notes:       
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#ifndef _TIMER_H
#define _TIMER_H


#include "mutex.h"


enum TimerState { Idle, Active, Done };
enum TimerType  { OneShot, Periodic };

class Timer
{
    public:

        Timer();
        ~Timer();

        int   start(unsigned int nMilliseconds, TimerType = OneShot);
        int   waitfor();
        void  cancel();

        TimerState    state;
        TimerType     type;
        unsigned int  length;

        unsigned int  count;
        Timer *       pNext;

        Mutex *       pMutex;         // Just planning ahead for Chapter 9.

    private:

        static void interrupt  Interrupt();
};


#endif /* _TIMER_H */
