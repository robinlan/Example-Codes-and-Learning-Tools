/**********************************************************************
 *
 * Filename:    serial.h
 * 
 * Description: Header file for the serial port class.
 *
 * Notes:       
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#ifndef _SERIAL_H
#define _SERIAL_H


#define PORTA    0
#define PORTB    1

#include "circbuf.h"

#ifdef putchar
#undef putchar
#endif

#ifdef getchar
#undef getchar
#endif

class SerialPort
{
    public:

        SerialPort(int            port,
                   unsigned long  baudRate = 19200L,
                   unsigned int   txQueueSize = 64,
                   unsigned int   rxQueueSize = 64);
        ~SerialPort();

        int        putchar(int c);
        int        puts(const char * s);

        int        getchar();
        char *     gets(char *s);

    private:
 
        int        channel;

        CircBuf *  pTxQueue;
        CircBuf *  pRxQueue;
};


#endif /* _SERIAL_H */
