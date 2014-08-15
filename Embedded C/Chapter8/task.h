/**********************************************************************
 *
 * Filename:    task.h
 * 
 * Description: Header file for the Task class.
 *
 * Notes:       
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#ifndef _TASK_H
#define _TASK_H


typedef unsigned char TaskId;
typedef unsigned char Priority;

enum TaskState { Ready, Running, Waiting };

class Task
{
    public:
  
        Task(void (*function)(), Priority, int stackSize);

        TaskId         id;
        Priority       priority;
        TaskState      state;
        Context        context;
        int *		   pStack;
        Task *         pNext;

        void  (*entryPoint)();

    private:

        static TaskId  nextId;
};


class TaskList
{
    public:

        TaskList();

        void    insert(Task * pTask);
        Task *  remove(Task * pTask);
 
        Task *  pTop;
};


#endif	/* _TASK_H */
