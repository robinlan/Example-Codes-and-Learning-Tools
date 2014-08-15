/**********************************************************************
 *
 * Filename:    sched.h
 * 
 * Description: Header file for the Sched class.
 *
 * Notes:       
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#ifndef _SCHED_H
#define _SCHED_H


class Sched
{
    public:

        Sched();

        void  start();
        void  schedule();

        void  enterIsr();
        void  exitIsr();

        static Task *    pRunningTask;
        static TaskList  readyList; 

        enum SchedState { Uninitialized, Initialized, Started };

    private:

        static SchedState  state;
        static Task        idleTask;
        static int         interruptLevel;
        static int         bSchedule;
};

extern Sched os;


#endif	/* _SCHED_H */
