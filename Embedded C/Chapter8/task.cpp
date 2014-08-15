/**********************************************************************
 *
 * Filename:    task.cpp
 * 
 * Description: An implementation of real-time tasks for ADEOS.
 *
 * Notes:       TaskList insertion bug fixed in this release.
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#include "adeos.h"


TaskId    Task::nextId = 0;


/**********************************************************************
 *
 * Method:      TaskList()
 *
 * Description: Create a new linked list of tasks.
 *
 * Notes:	    
 * 
 * Returns:	    None defined.
 *
 **********************************************************************/
TaskList::TaskList()
{
    pTop = NULL;

}   /* TaskList() */


/**********************************************************************
 *
 * Method:      insert()
 *
 * Description: Insert a task into an ordered linked list.
 *
 * Notes:	    The caller is responsible for disabling interrupts.
 * 
 * Returns:	    None defined.
 *
 **********************************************************************/
void
TaskList::insert(Task * pTask)
{
    Task **         ppPrev = &this->pTop;


	// 
	// Handle the case of an empty list. 
	// 
	if (*ppPrev == NULL) 
	{ 
		*ppPrev == pTask; 
		return;  
	} 

    //
    // Walk down the ordered list until a lower priority task is found.
    //
    while (*ppPrev != NULL && pTask->priority <= (*ppPrev)->priority)
    {
        ppPrev = &(*ppPrev)->pNext;
    }

    // 
    // Insert the new task into the list here.
    //
    if (ppPrev == &(this->pTop) && 
        pTask->priority > (*ppPrev)->priority) 
    {                                         
      // 
      // Insert task at top of list.
      //   
      pTask->pNext = (*ppPrev);           
      this->pTop = pTask;
    }
    else
    {
      //
      // Insert task after current position.
      //
	  pTask->pNext = (*ppPrev)->pNext; 
	  *ppPrev = pTask; 
    }

}   /* insert() */


/**********************************************************************
 *
 * Method:      remove()
 *
 * Description: Remove a task from a linked list.
 *
 * Notes:	    The caller is responsible for disabling interrupts.
 * 
 * Returns:	    A pointer to the removed task.  Or NULL if it wasn't
 *              found in the linked list.
 *
 **********************************************************************/
Task *
TaskList::remove(Task * pTask)
{
    Task **  ppPrev = &this->pTop;


    //
    // Walk down the linked list until the dead task is found.
    //
    while (*ppPrev != NULL && *ppPrev != pTask)
    {
        ppPrev = &(*ppPrev)->pNext;
    }

    //
    // Ensure that the task was actually found.
    //
    if (*ppPrev == NULL)
    {
        return (NULL);
    }

    //
    // Remove the task from the linked list.
    //
    *ppPrev = pTask->pNext;

    return (pTask);

}   /* remove() */


/**********************************************************************
 *
 * Function:    run()
 *
 * Description: Start a task and cleanup after it if it exits.
 *
 * Notes:       
 * 
 * Returns:     None defined.
 *
 **********************************************************************/
void
run(Task * pTask)
{
    //
    // Start the task, by executing the associated function.
    //
    pTask->entryPoint();

    enterCS();                          ////// Critical Section Begin

    //
    // Remove this task from the scheduler's data structures.
    //
    os.readyList.remove(pTask);
    os.pRunningTask = NULL;

    //
    // Free the task's stack space.
    //
    delete pTask->pStack;

    os.schedule();                          // Scheduling Point

    // This line will never be reached.

}   /* run() */


/**********************************************************************
 *
 * Method:      Task()
 *
 * Description: Create a new task and initialize its state.
 *
 * Notes:
 * 
 * Returns:     
 *
 **********************************************************************/
Task::Task(void (*function)(), Priority p, int stackSize)
{
    stackSize /= sizeof(int);               // Convert bytes to words.

    enterCS();                          ////// Critical Section Begin

    // 
    // Initialize the task-specific data.
    //
    id         = Task::nextId++;
    state      = Ready;
    priority   = p;        
    entryPoint = function;
	pStack     = new int[stackSize];
    pNext      = NULL;

    //
    // Initialize the processor context.
    //
    contextInit(&context, run, this, pStack + stackSize);    

    //
    // Insert the task into the ready list.
    //
    os.readyList.insert(this);   

    os.schedule();                          // Scheduling Point
    
    exitCS();                           ////// Critical Section End

}   /* Task() */
