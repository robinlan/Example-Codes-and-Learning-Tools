/**********************************************************************
 *
 * Filename:    i8018xEB.h
 * 
 * Description: Header file for the Intel 8018xEB embedded processor.  
 *
 * Notes:       
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#ifndef _i8018xEB_H
#define _i8018xEB_H


struct InterruptController
{
    unsigned short       eoi;
    unsigned short       poll;
    unsigned short       pollStatus;
    unsigned short       intMask;
    unsigned short       priorityMask;
    unsigned short       inService;
    unsigned short       intRequest;
    unsigned short       intStatus;
    unsigned short       timerControl;
    unsigned short       serialControl;
    unsigned short       int4Control;
    unsigned short       int0Control;
    unsigned short       int1Control;
    unsigned short       int2Control;
    unsigned short       int3Control;
};

#define EOI_NONSPECIFIC	 0x8000

#define INTMASK_TIMER    0x0001
#define INTMASK_SERIAL   0x0004
#define INTMASK_INT4     0x0008
#define INTMASK_INT0     0x0010
#define INTMASK_INT1     0x0020
#define INTMASK_INT2     0x0040
#define INTMASK_INT3     0x0080

#define TIMER_MASK       0x0008
#define TIMER_PRIORITY	 0x0007

#define SERIAL_MASK      0x0008
#define SERIAL_PRIORITY	 0x0007

#define EXTINT_MASK      0x0008
#define EXTINT_PRIORITY  0x0007


struct TimerCounter
{
    unsigned short       count;
    unsigned short       maxCountA;
    unsigned short       maxCountB;
    unsigned short       control;
};

#define TIMER_ENABLE	 0xC000
#define TIMER_DISABLE	 0x4000
#define TIMER_INTERRUPT	 0x2000
#define TIMER_MAXCOUNT   0x0020
#define TIMER_PERIODIC	 0x0001

#define TIMER0_INT        8
#define TIMER1_INT       18
#define TIMER2_INT       19


struct InputOutputPort
{
    unsigned short       direction;
    unsigned short       state;
    unsigned short       control;
    unsigned short       latch;
};

struct SerialController
{
    unsigned short       baudCompare;
    unsigned short       baudCount;
    unsigned short       control;
    unsigned short       status;
    unsigned short       receive;
    unsigned short       transmit;
};


struct ChipSelect
{
    unsigned short       start;
    unsigned short       stop;
};


struct RefreshController
{
    unsigned short       baseAddr;
    unsigned short       clockInterval;
    unsigned short       control;
    unsigned short       address;
};


struct PeripheralControlBlock 
{
    unsigned short       reserved0;
    InterruptController  intControl;
    unsigned short       reserved1[8];
    TimerCounter         timer[3];
    unsigned short       reserved2[4];
    InputOutputPort      ioPort[2];
    SerialController	 serialPort[2];
    ChipSelect           gcs[8];
    ChipSelect           lcs;
    ChipSelect           ucs;
    unsigned short       pcbLocation;
    unsigned short       reserved3[3];
    RefreshController    refreshControl;
    unsigned short       powerControl;
    unsigned short       reserved4;
    unsigned short       stepId;
    unsigned short       reserved5;
    unsigned short       reserved6[32];
};


class i8018xEB
{
    public:

        i8018xEB();

        void installHandler(unsigned char  nInterrupt, 
                            void interrupt (*handler)());

        static volatile PeripheralControlBlock * pPCB;

    private:

        static unsigned long * intVectorTable;
};


extern i8018xEB gProcessor;


#endif /* _i8018xEB_H */
