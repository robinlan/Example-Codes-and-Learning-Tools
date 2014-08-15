/**********************************************************************
 *
 * Filename:    scc.h
 * 
 * Description: Constants and macros for the Zilog 85230 SCC.
 *
 * Notes:       
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#ifndef _SCC_H
#define _SCC_H


#include "circbuf.h"

class SCC
{
    public:
        
        SCC();

        void  reset(int channel);
        void  init(int channel, unsigned long baudRate,
                   CircBuf * pTxQueue, CircBuf * pRxQueue);

        void  txStart(int channel);
        void  rxStart(int channel);

    private:
    
        static void interrupt  Interrupt(void);
};


#endif /* _SCC_H */
