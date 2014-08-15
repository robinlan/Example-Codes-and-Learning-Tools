/**********************************************************************
 *
 * Filename:    sched.cpp
 * 
 * Description: An implementation of premptive scheduling for ADEOS.
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

#include "adeos.h"                    


Sched::SchedState  Sched::state = Uninitialized;

int       Sched::interruptLevel = 0;
int       Sched::bSchedule      = 0;

Task *    Sched::pRunningTask = NULL;
TaskList  Sched::readyList;

Sched     os;

Task      Sched::idleTask(idle, 0, 128);


/**********************************************************************
 *
 * Method:      Sched()
 *
 * Description: Initialize the operating system.
 *
 * Notes:       Must be called before any other routine in the OS!
 * 
 * Returns:     None defined.
 *
 **********************************************************************/
Sched::Sched(void)
{
    if (state != Uninitialized) return;

    state = Initialized;

}   /* Sched() */


/**********************************************************************
 *
 * Method:      start()
 *
 * Description:	Start multitasking.
 *
 * Notes:       
 * 
 * Returns:     This routine isn't supposed to return.  If it does,
 *              it means that the OS was not properly initialized.
 *
 **********************************************************************/
void
Sched::start(void)
{
    if (state != Initialized) return;

    state = Started;
	
    schedule();                             // Scheduling Point

    // This line will never be executed.

}   /* start() */


/**********************************************************************
 *
 * Method:      schedule()
 *
 * Description: Select a new task to be run.
 *
 * Notes:       If this routine is called from within an ISR, the 
 *              schedule will be postponed until the nesting level
 *              returns to zero.
 *
 *              The caller is responsible for disabling interrupts.
 * 
 * Returns:     None defined.
 *
 **********************************************************************/
void 
Sched::schedule(void)
{
    Task *  pOldTask;
    Task *  pNewTask;


    if (state != Started) return;

    //
    // Postpone rescheduling until interrupts are completed.
    //
    if (interruptLevel != 0) 
    {
        bSchedule = 1;
        return;
    }

    //
    // If there is a higher-priority ready task, switch to it.
    //
    if (pRunningTask != readyList.pTop) 
    {
        pOldTask = pRunningTask;
        pNewTask = readyList.pTop;

        pNewTask->state = Running;
        pRunningTask = pNewTask;

        if (pOldTask == NULL)
        {
            contextSwitch(NULL, &pNewTask->context);
        }
        else
        {
            pOldTask->state = Ready;
            contextSwitch(&pOldTask->context, &pNewTask->context);
        }
    }

}   /* schedule() */


/**********************************************************************
 *
 * Method:      enterIsr()
 *
 * Description:	Alert the OS of an interrupt service routine entry.
 *
 * Notes:       This is necessary so that the OS will only schedule 
 *              once--after the interrupt nesting level reaches zero.
 * 
 * Returns:     None defined.
 *
 **********************************************************************/
void 
Sched::enterIsr(void)
{
    enterCS();                          ////// Critical Section Begin

    interruptLevel++;

    exitCS();                           ////// Critical Section End

}   /* enterIsr() */


/**********************************************************************
 *
 * Method:      exitIsr()
 *
 * Description: Alert the OS of an interrupt service routine exit.
 *
 * Notes:       This is necessary so that the OS will only schedule 
 *              once--after the interrupt nesting level reaches zero.
 * 
 * Returns:     None defined.
 *
 **********************************************************************/
void 
Sched::exitIsr(void)
{
    enterCS();                          ////// Critical Section Begin

    interruptLevel--;

    if (interruptLevel == 0 && bSchedule) 
    {
        bSchedule = 0;
        schedule();                         // Scheduling Point
    }

    exitCS();                           ////// Critical Section End

}   /* exitIsr() */
