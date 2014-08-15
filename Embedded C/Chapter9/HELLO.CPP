/**********************************************************************
 *
 * Filename:    hello.cpp
 * 
 * Description: A "Hello, World!" application built on top of ADEOS.
 *
 * Notes:       This module is hardware-independent.  All of the 
 *              hardware dependence has been captured in the device
 *              driver and operating system modules.
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#include "adeos.h"

#include "led.h"
#include "timer.h"
#include "serial.h"


/**********************************************************************
 *
 * Function:    flashRed()
 *
 * Description: Blink the red LED ten times a second.
 * 
 * Notes:       This outer loop is hardware-independent.  However, it 
 *              calls the hardware-dependent function toggleLed().
 *
 * Returns:     This routine contains an infinite loop.
 *
 **********************************************************************/
void
flashRed(void)
{
    Timer  timer;


    timer.start(50, Periodic);        // Start a periodic 50-ms timer.

    while (1)
    {
        toggleLed(LED_RED);           // Toggle the red LED.
        timer.waitfor();              // Wait for the timer to expire.
    }

}   /* flashRed() */


/**********************************************************************
 *
 * Function:    helloWorld()
 * 
 * Description: Send a text message to the serial port periodically.
 *
 * Notes:       This outer loop is hardware-independent.  
 *
 * Returns:     This routine contains an infinite loop.
 *
 **********************************************************************/
void
helloWorld(void)
{
    Timer       timer;
    SerialPort  serial(PORTA, 19200L);


    timer.start(10000, Periodic);      // Start a periodic 10-s timer.

    while (1)
    {
        serial.puts("Hello, World!");  // Output a simple text message.
        timer.waitfor();               // Wait for the timer to expire.
    }

}   /* helloWorld() */


Task  taskA(flashRed,   150, 512);
Task  taskB(helloWorld, 200, 512);


/*********************************************************************
 * 
 * Function:    main()
 *
 * Description: This function is responsible for starting the ADEOS
 *              scheduler only.
 *
 * Notes:       
 *
 * Returns:     This function will never return!
 *
 *********************************************************************/
void
main(void)
{
    os.start();

    // This point will never be reached.

}   /* main() */
