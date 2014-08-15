/**********************************************************************
 *
 * Filename:    mutex.cpp
 * 
 * Description: An implementation of binary semaphores for ADEOS.
 *
 * Notes:       
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#include "adeos.h" 


/**********************************************************************
 *
 * Method:      Mutex()
 *
 * Description: Create a new mutex.
 *
 * Notes:
 * 
 * Returns:     
 *
 **********************************************************************/
Mutex::Mutex()
{
    enterCS();                          ////// Critical Section Begin

    state = Available;
    waitingList.pTop = NULL;

    exitCS();                           ////// Critical Section End
	
}   /* Mutex() */


/**********************************************************************
 *
 * Method:      take()
 *
 * Description: Wait for a mutex to become available, then take it.
 *
 * Notes:
 * 
 * Returns:     None defined.
 *
 **********************************************************************/
void
Mutex::take(void)
{
    Task *  pCallingTask;


    enterCS(); 	                        ////// Critical Section Begin

    if (state == Available)
    {
        //
        // The mutex is available.  Simply take it and return.
        //
        state = Held;
        waitingList.pTop = NULL;
    }
    else
    {
        //
        // The mutex is taken.  Add the calling task to the waiting list.
        //
        pCallingTask = os.pRunningTask;
        pCallingTask->state = Waiting;
        os.readyList.remove(pCallingTask);
        waitingList.insert(pCallingTask);

        os.schedule();                       // Scheduling Point

        // When the mutex is released, the caller begins executing here.
    }

    exitCS();                           ////// Critical Section End

}   /* take() */
 

/**********************************************************************
 *
 * Method:      release()
 *
 * Description: Release a mutex that is held by the calling task.
 *
 * Notes:
 * 
 * Returns:     None defined.
 *
 **********************************************************************/
void
Mutex::release(void)
{
    Task *  pWaitingTask;


    enterCS();                          ////// Critical Section Begins

    if (state == Held)
    {
        pWaitingTask = waitingList.pTop;

        if (pWaitingTask != NULL)
        {
            //
            // Wake the first task on the waiting list.
            //
            waitingList.pTop = pWaitingTask->pNext;
            pWaitingTask->state = Ready;
            os.readyList.insert(pWaitingTask);  

            os.schedule();                  // Scheduling Point
        }
        else
        {
            state = Available;
        }
    }

    exitCS();                           ////// Critical Section End

}   /* release() */
