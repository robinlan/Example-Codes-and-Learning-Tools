/**********************************************************************
 *
 * Filename:    timer.cpp
 * 
 * Description: A software timer class implemented over top of the
 *              hardware timer within the Intel 8018xEB processor.
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

#include "i8018xEB.h"
#include "adeos.h"

#include "timer.h"


#define CYCLES_PER_TICK 62500      // Number of clock cycles per tick.
#define MS_PER_TICK     10         // Number of milliseconds per tick.


class TimerList
{
    public:

        TimerList();

        void     insert(Timer * pTimer);
        Timer *  remove(Timer * pTimer);

        void     tick(void);
 
    private:

        Timer *  pTop;

};

TimerList timerList;


/**********************************************************************
 *
 * Function:    TimerList()
 * 
 * Description: Create and initialize a linked list of timers.
 *
 * Notes:       
 *
 * Returns:     None defined.
 *
 **********************************************************************/
TimerList::TimerList(void)
{
    pTop = NULL;

}   /* TimerList() */


/**********************************************************************
 *
 * Method:      insert()
 *
 * Description: Insert a timer into an ordered linked list.
 *
 * Notes:       This routine disables interrupts.
 * 
 * Returns:     None defined.
 *
 **********************************************************************/
void
TimerList::insert(Timer * pTimer)
{
    Timer **  ppPrev = &this->pTop;


    enterCS();

    //
    // Initialize the new timer's tick count.
    //
    pTimer->count = pTimer->length;

    //
    // Walk down the timer list, subtracting ticks as we go.
    //
    while (*ppPrev != NULL && pTimer->count >= (*ppPrev)->count)
    {
        pTimer->count -= (*ppPrev)->count;
        ppPrev = &(*ppPrev)->pNext;
    }
    
    // 
    // Insert the new timer at this point in the timer list.
    //
    pTimer->pNext = *ppPrev;
    *ppPrev = pTimer;
  
    //
    // Adjust the tick count of the next timer (if any).
    //
    if (pTimer->pNext != NULL)
    {
        pTimer->pNext->count -= pTimer->count;
    } 

    exitCS();

}   /* insert() */


/**********************************************************************
 *
 * Method:      remove()
 *
 * Description: Remove a timer from the timer list.
 *
 * Notes:       This routine disables interrupts.
 * 
 * Returns:     A pointer to the removed timer, NULL if it wasn't
 *              found in the timer list.
 *
 **********************************************************************/
Timer *
TimerList::remove(Timer * pTimer)
{
    Timer **  ppPrev = &this->pTop;


    enterCS();

    //
    // Walk down the linked list until the dead timer is found.
    //
    while (*ppPrev != NULL && *ppPrev != pTimer)
    {
        ppPrev = &(*ppPrev)->pNext;
    }

    //
    // Remove the dead timer from the linked list.
    //
    if (*ppPrev != NULL)
    {
        *ppPrev = pTimer->pNext;
        (*ppPrev)->count += pTimer->count;
    }

    exitCS();

    return (*ppPrev);

}   /* remove() */


/**********************************************************************
 *
 * Method:      tick()
 *
 * Description: Update the linked list of timers for a clock tick.
 *
 * Notes:       The caller is responsible for disabling interrupts.
 * 
 * Returns:     A pointer to the removed timer.  Or NULL if it wasn't
 *              found in the linked list.
 *
 **********************************************************************/
void
TimerList::tick()
{
    Timer **  ppTop  = &this->pTop;


    if (*ppTop != NULL)
    {
        //
        // Decrement the tick count of the first timer in the list.
        //
        (*ppTop)->count--;

        //
        // Mark all of the expired timers done and remove them.
        //
        while (*ppTop != NULL && (*ppTop)->count == 0)
        {
            (*ppTop)->state = Done;
            (*ppTop)->pMutex->release();

            *ppTop = (*ppTop)->pNext;
        }                                   
    }

}   /* tick() */


/**********************************************************************
 * 
 * Method:      Interrupt()
 *
 * Description: An interrupt handler for the timer hardware.
 *
 * Notes:       This method is declared static, so that we cannot 
 *              inadvertently modify any of the software timers.
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void interrupt
Timer::Interrupt()
{
    os.enterIsr();

    //
    // Decrement the active timer's count.
    //
    timerList.tick();

    //
    // Acknowledge the timer interrupt.
    //
    gProcessor.pPCB->intControl.eoi = EOI_NONSPECIFIC;

    //
    // Clear the Maximum Count bit (to start the next cycle). 
    //
    gProcessor.pPCB->timer[2].control &= ~TIMER_MAXCOUNT;

    os.exitIsr();

}   /* Interrupt() */


/**********************************************************************
 * 
 * Method:      Timer()
 *
 * Description: Constructor for the Timer class.
 *
 * Notes:    
 *
 * Returns:     None defined.
 *
 **********************************************************************/
Timer::Timer(void)
{
    static int  bInitialized = 0;


    //
    // Initialize the new software timer.
    //
    state  = Idle;
    type   = OneShot;
    length = 0;
    count  = 0;
    pNext  = NULL;
    pMutex = new Mutex;

    //
    // Initialize the timer hardware, if not previously done.
    //
    if (!bInitialized)
    {
        //
        // Install the interrupt handler and enable timer interrupts.
        //
        gProcessor.installHandler(TIMER2_INT, Timer::Interrupt);
        gProcessor.pPCB->intControl.timerControl &= 
                                 ~(TIMER_MASK | TIMER_PRIORITY);

        // 
        // Initialize the hardware device (use Timer #2).
        // 
        gProcessor.pPCB->timer[2].count = 0;
        gProcessor.pPCB->timer[2].maxCountA = CYCLES_PER_TICK;
        gProcessor.pPCB->timer[2].control = TIMER_ENABLE 
                                          | TIMER_INTERRUPT
                                          | TIMER_PERIODIC;

        //
        // Mark the timer hardware initialized.
        //
        bInitialized = 1;
    }

}   /* Timer() */


/**********************************************************************
 * 
 * Method:      ~Timer()
 *
 * Description: Destructor for the Timer class.
 *
 * Notes:       This routine ensures that the timer isn't left dangling
 *              in the timer list and that its mutex is freed.
 *
 * Returns:     None defined.
 *
 **********************************************************************/
Timer::~Timer(void)
{
    //
    // Cancel the timer.
    //
    this->cancel();

    // 
    // Free the mutex. 
    //
    delete pMutex;

}   /* ~Timer() */


/**********************************************************************
 * 
 * Method:      start()
 *
 * Description: Start a software timer, based on the tick from the
 *              underlying hardware timer.
 *
 * Notes:    
 *
 * Returns:     0 on success, -1 if the timer is already in use.
 *
 **********************************************************************/
int
Timer::start(unsigned int nMilliseconds, TimerType timerType)
{
    if (state != Idle)
    {
        return (-1);
    }

    //
    // Take the mutex.  It will be released when the timer expires.
    //
    pMutex->take();

    //
    // Initialize the timer.
    //
    type   = timerType;
    length = nMilliseconds / MS_PER_TICK;
    state  = Active;

    //
    // Add the timer to the timer list.
    //
    timerList.insert(this);

    return (0);

}   /* start() */


/**********************************************************************
 * 
 * Method:      waitfor()
 *
 * Description: Wait for the software timer to finish.
 *
 * Notes:    
 *
 * Returns:     0 on success, -1 if the timer is not running.
 *
 **********************************************************************/
int
Timer::waitfor()
{
    if (state != Active)
    {
        return (-1);
    }

    //
    // Wait for the timer to expire.
    //
    pMutex->take();

    //
    // Restart or idle the timer, depending on its type.
    //
    if (type == Periodic)
    {
        state = Active;
        timerList.insert(this);
    }
    else
    {
        pMutex->release();
        state = Idle;
    }

    return (0);

}   /* waitfor() */


/**********************************************************************
 * 
 * Method:      cancel()
 *
 * Description: Stop a running timer.
 *
 * Notes:
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void
Timer::cancel(void)
{
    // 
    // Remove the timer from the timer list.
    //
    if (state == Active)
    {
        timerList.remove(this);
    }

    //
    // Reset the timer's state.
    //
    state = Idle; 

    //
    // Release the mutex.
    //
    pMutex->release();

}   /* cancel() */
