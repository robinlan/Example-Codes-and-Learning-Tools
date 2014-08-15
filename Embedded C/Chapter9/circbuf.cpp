/**********************************************************************
 *
 * Filename:    circbuf.cpp
 * 
 * Description: An easy-to-use circular buffer class.
 *
 * Notes:       Relies on the Mutex class to protect buffer contents.
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#include "adeos.h"
#include "circbuf.h"


/**********************************************************************
 *
 * Method:      CircBuf()
 * 
 * Description: Create a circular buffer.
 *
 * Notes:       
 *
 * Returns:     None defined.
 *
 **********************************************************************/
CircBuf::CircBuf(int nItems)
{
    size  = nItems;
    array = new item[size];
    head  = 0;
    tail  = 0;
    count = 0;

}   /* CircBuf() */


/**********************************************************************
 *
 * Method:      ~CircBuf()
 * 
 * Description: Destroy a circular buffer.
 *
 * Notes:       
 *
 * Returns:     None defined.
 *
 **********************************************************************/
CircBuf::~CircBuf(void)
{
    delete array;

}   /* ~CircBuf() */


/**********************************************************************
 *
 * Method:      add()
 * 
 * Description: Add an item to the buffer.
 *
 * Notes:       It is up to the caller to check isFull() first, since
 *              there is no convenient way to indicate that error.
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void
CircBuf::add(item i)
{
    mutex.take();

    if (!isFull())
    {
        array[head] = i;
        head = (head + 1) % size;
        count++;
    }
  
    mutex.release();

}   /* add() */


/**********************************************************************
 *
 * Method:      remove()
 * 
 * Description: Remove an item from the buffer.
 *
 * Notes:       It is up to the caller to check isEmpty() first, since
 *              there is no convenient way to indicate that error.
 *
 * Returns:     The removed item.
 *
 **********************************************************************/
item
CircBuf::remove(void)
{
    item  i;


    mutex.take();

    if (!isEmpty())
    {
        i = array[tail];
        tail = (tail + 1) % size;
        count--;
    }

    mutex.release();

    return (i);

}   /* remove() */
