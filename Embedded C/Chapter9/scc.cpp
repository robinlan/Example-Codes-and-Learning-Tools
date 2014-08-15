/**********************************************************************
 *
 * Filename:    scc.cpp
 * 
 * Description: Implementation of the Zilog 85230-specific SCC class.  
 *              This class abstracts the behavior of a general serial 
 *              communications controller (two channels).
 *
 * Notes:       Most of the constants in this file are specific to 
 *              Zilog's 85230 SCC chip.
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#include "adeos.h"
#include "circbuf.h"

#include "i8018xEB.h"
#include "tgt188eb.h"

#include "scc.h"


struct Z85230
{
    unsigned char  cmdB;
    unsigned char  cmdA;
    unsigned char  dataB;
    unsigned char  dataA;
};

volatile Z85230 * pSCC         = (Z85230 *) SCC_BASE;
volatile char   * pAcknowledge = (char *)   SCC_INTACK;

/*
 * Read Register 0 (Transmit/Receive Buffer Status and Ext Status)
 */
#define TX_READY   0x04
#define RX_READY   0x01

/*
 * Read Register 3 (Interrupt Pending Bits (Channel A Only))
 */
#define CHANNEL_A_TX_INTERRUPT  0x10
#define CHANNEL_A_RX_INTERRUPT  0x20
#define CHANNEL_B_TX_INTERRUPT  0x02
#define CHANNEL_B_RX_INTERRUPT  0x04

/*
 * Write Register 1 (Transmit/Receive Interrupt and Mode Definition)
 */
#define TX_INT_ENABLE       0x02
#define RX_INT_ENABLE       0x10

/*
 * Write Register 3 (Receive Parameters and Controls)
 */
#define RX_BITS_5           0x00
#define RX_BITS_6           0x80
#define RX_BITS_7           0x40
#define RX_BITS_8           0xC0

#define RX_ENABLE           0x01

/*
 * Write Register 4 (Transmit/Receive Misc Parameters and Modes)
 */
#define PARITY_NONE         0x00
#define PARITY_ODD          0x02
#define PARITY_EVEN         0x03

#define STOPBITS_1          0x04

#define CLOCK_1X            0x00
#define CLOCK_16X           0x40
#define CLOCK_32X           0x80
#define CLOCK_64X           0xC0

/*
 * Write Register 5 (Transmit Parameters and Controls)
 */
#define TX_BITS_5           0x00
#define TX_BITS_6           0x40
#define TX_BITS_7           0x20
#define TX_BITS_8           0x60

#define TX_ENABLE           0x08

/*
 * Write Register 9 (Master Interrupt Control)
 */
#define CHANNEL_A_RESET     0x80
#define CHANNEL_B_RESET     0x40
#define HARDWARE_RESET      0xC0

#define ENABLE_INTERRUPTS   0x08

/*
 * Write Register 10 (Misc Transmit/Receive Control Bits)
 */
#define NRZ_ENCODING        0x00

/*
 * Write Register 11 (Clock Mode Controls)
 */
#define RX_BRG              0x40
#define TX_BRG              0x10

/*
 * Write Register 14 (Misc Control Bits)
 */
#define BRG_FROM_PCLK       0x02
#define BRG_ENABLE          0x01
#define LOOPBACK            0x10


static inline void
writeCommand(int channel, unsigned char command)
{
    if (channel == 0) pSCC->cmdA = command;
    else              pSCC->cmdB = command;

}    /* writeCommand() */


static inline void
writeRegister(int channel, unsigned char reg, unsigned char value)
{
    if (channel == 0)
    {
        pSCC->cmdA = reg;
        pSCC->cmdA = value;
    }
    else
    {
        pSCC->cmdB = reg;
        pSCC->cmdB = value;
    }

}    /* writeRegister() */


static inline void
writeData(int channel, unsigned char data)
{
    if (channel == 0) pSCC->dataA = data;
    else              pSCC->dataB = data;

}    /* writeData() */


static inline unsigned char
readData(int channel)
{
    return ((channel == 0) ? pSCC->dataA : pSCC->dataB);
    
}    /* readData() */


static inline unsigned char
readRegister(int channel, unsigned char reg)
{
    if (channel == 0)
    {
        pSCC->cmdA = reg;
        return (pSCC->cmdA);
    }
    else
    {
        pSCC->cmdB = reg;
        return (pSCC->cmdB);
    }

}    /* readRegister() */


CircBuf *  txQueue[2];
CircBuf *  rxQueue[2];


/**********************************************************************
 * 
 * Method:      Interrupt()
 *
 * Description: An interrupt handler for the Zilog 85230 SCC.
 *
 * Notes:       
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void interrupt
SCC::Interrupt(void)
{
    unsigned char  intStatus;


    os.enterIsr();

    //
    // Read the interrupt pending register (only in channel A).
    //
    intStatus = readRegister(0, 3);

    //
    // Process all pending interrupts.
    //
    if (intStatus & CHANNEL_A_TX_INTERRUPT)
    {
        //
        // Send data until the SCC is full or the buffer is empty.
        //
        while ((readRegister(0, 0) & TX_READY) && !txQueue[0]->isEmpty())
        {
            writeData(0, txQueue[0]->remove());    
        }
    }

    if (intStatus & CHANNEL_A_RX_INTERRUPT)
    {
    }
    
    if (intStatus & CHANNEL_B_TX_INTERRUPT)
    {
        //
        // Send data until the SCC is full or the buffer is empty.
        //
        while ((readRegister(1, 0) & TX_READY) && !txQueue[1]->isEmpty())
        {
            writeData(1, txQueue[1]->remove());    
        }
    }
    
    if (intStatus & CHANNEL_B_RX_INTERRUPT)
    {
    }
    
    //
    // Acknowledge the interrupt.
    //
    *pAcknowledge = 0;
    gProcessor.pPCB->intControl.eoi = EOI_NONSPECIFIC;

    os.exitIsr();

}   /* Interrupt() */


/**********************************************************************
 *
 * Method:      SCC()
 * 
 * Description: Create a new Zilog 85230 SCC object.
 *
 * Notes:       There is only one of these devices on the Arcom board.
 *
 * Returns:     None defined.
 *
 **********************************************************************/
SCC::SCC(void)
{
    enterCS();

    //
     // Reset the Zilog 85230 SCC hardware (both channels).
    //
    writeRegister(0, 9, HARDWARE_RESET);

    //
    // Install the interrupt handler, and enable SCC interrupts.
    //
    gProcessor.installHandler(SCC_INT, SCC::Interrupt);
    gProcessor.pPCB->intControl.int4Control &= 
                                ~(EXTINT_MASK | EXTINT_PRIORITY);

    exitCS();

}   /* z85230() */


/**********************************************************************
 *
 * Method:      reset()
 * 
 * Description: Reset the serial channel.
 *
 * Notes:       
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void
SCC::reset(int channel)
{
    enterCS();

    writeRegister(0, 9, (channel == 0) ? CHANNEL_A_RESET : CHANNEL_B_RESET);

    exitCS();

}   /* reset() */


/**********************************************************************
 *
 * Method:      init()
 * 
 * Description: Initialize one of the serial channels for asynchronous
 *              communications.
 *
 * Notes:       Only the baud rate is selectable.  All communications
 *              are assumed to be 8 data bits, no parity, 1 stop bit.
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void
SCC::init(int channel, unsigned long baudRate,
                 CircBuf * pTxQueue, CircBuf * pRxQueue)
{
    enterCS();

    //
    // Select asynchronous mode (by setting number of stop bits).
    //
    writeRegister(channel, 4, CLOCK_16X | STOPBITS_1 | PARITY_NONE);

    //
    // Configure receive and transmit for 8-bits/char.
    //
    writeRegister(channel, 3, RX_BITS_8);
    writeRegister(channel, 5, TX_BITS_8);

    //
     // Select NRZ encoding for output.
    //
    writeRegister(channel, 10, NRZ_ENCODING);        

    //
     // Select the internal baud rate generator (BRG) as bit clock.
    //
    writeRegister(channel, 11, RX_BRG | TX_BRG);    

    //
    // Set the BRG time constant for the indicated baud rate.
    //
    unsigned long  timeConstant = (SCC_CLOCK / (2 * baudRate * 16)) - 2;

    writeRegister(channel, 12, (timeConstant & 0x00FF));
    writeRegister(channel, 13, (timeConstant & 0xFF00) >> 8);

    //
    // Select the PCLK as oscillator and enable the internal BRG.
    //
    writeRegister(channel, 14, BRG_FROM_PCLK);
    writeRegister(channel, 14, BRG_FROM_PCLK | BRG_ENABLE);        
    
    //
      // Enable interrupts within the SCC.
     //
    writeRegister(channel, 1, TX_INT_ENABLE);        
    writeRegister(channel, 9, ENABLE_INTERRUPTS);    

    //
    // Formally enable receive and transmit.
    //
    writeRegister(channel, 3, RX_ENABLE | RX_BITS_8);
    writeRegister(channel, 5, TX_ENABLE | TX_BITS_8);

    //
    // Copy the input and output buffer pointers.
    //
    txQueue[channel] = pTxQueue;
    rxQueue[channel] = pRxQueue;

    exitCS();

}   /* init() */


/**********************************************************************
 * 
 * Method:      txStart()
 *
 * Description: Kickstart the transmit process after a previous stall.
 *
 * Notes:       It's okay if this gets called too often, since it will
 *              always check the TX_READY flag before writing.  If the
 *              TX_READY flag isn't set, nothing will be done here.
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void
SCC::txStart(int channel)
{
    enterCS();

    //
    // Send data until the SCC is full or the buffer is empty.
    //
    while ((readRegister(channel, 0) & TX_READY) && 
           !txQueue[channel]->isEmpty())
    {
        writeData(channel, txQueue[channel]->remove());    
    }

    exitCS();    
    
}   /* txStart() */


/**********************************************************************
 * 
 * Method:      rxStart()
 *
 * Description: Kickstart the receive process after a previous stall.
 *
 * Notes:       The caller should ensure that there is room in the 
 *              rxQueue, since isFull() isn't checked here.
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void
SCC::rxStart(int channel)
{
    enterCS();

    //
    // Wait for data to appear in the receive buffer.
    //
    while (!(readRegister(channel, 0) & RX_READY));
    
    //
    // Read the next character from the serial port.
    //
    rxQueue[channel]->add(readData(channel));

    exitCS();
    
}   /* rxStart() */
