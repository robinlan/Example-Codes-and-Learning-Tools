/**********************************************************************
 *
 * Filename:    blink.c
 * 
 * Description: The embedded systems equivalent of "Hello, World".
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

#include "led.h"


/**********************************************************************
 *
 * Function:    delay()
 *
 * Description: Busy-wait for the requested number of milliseconds.
 *
 * Notes:       The number of decrement-and-test cycles per millisecond
 *              was determined through trial and error.  This value is
 *              dependent upon the processor type and speed.
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void 
delay(unsigned int nMilliseconds)
{
    #define CYCLES_PER_MS 260 /* Number of decrement-and-test cycles. */

    unsigned long nCycles = nMilliseconds * CYCLES_PER_MS;

    while (nCycles--);

}   /* delay() */


/**********************************************************************
 *
 * Function:    main()
 *
 * Description: Blink the green LED once a second.
 * 
 * Notes:       This outer loop is hardware-independent.  However, 
 *              it depends on two hardware-dependent functions.
 *
 * Returns:     This routine contains an infinite loop.
 *
 **********************************************************************/
void
main(void)
{
    while (1)
    {
        toggleLed(LED_GREEN);      /* Change the state of the LED.    */
        delay(500);                /* Pause for 500 milliseconds.     */
    }

}   /* main() */
