/**********************************************************************
 *
 * Filename:    tgt188eb.h
 * 
 * Description: Header file for Arcom's Target188EB hardware.
 *
 * Notes:       
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#ifndef _TGT188EB_H
#define _TGT188EB_H


/**********************************************************************
 *
 *  Memory Map
 *
 *            Base Address   Size  Description
 *            -------------- ----- ------------------------------------
 *            0000:0000h     128K  SRAM
 *            2000:0000h           Unused
 *            7000:0000h           Zilog SCC Registers
 *            7000:1000h           Zilog SCC Interrupt Acknowledge
 *            7000:2000h           Unused
 *            C000:0000h     128K  Flash
 *            E000:0000h     128K  EPROM
 *
 **********************************************************************/

#define SRAM_BASE       (void *) 0x00000000
#define SCC_BASE        (void *) 0x70000000
#define SCC_INTACK      (void *) 0x70001000
#define FLASH_BASE      (void *) 0xC0000000
#define EPROM_BASE      (void *) 0xE0000000


/**********************************************************************
 *
 *  I/O Map
 *
 *            Base Address    Description
 *            --------------- ----------------------------------------
 *            0000h           Unused      	
 *            FC00h           SourceVIEW Debugger Port (SVIEW)
 *            FD00h           Parallel I/O Port (PIO)
 *            FE00h           Unused
 *            FF00h           Peripheral Control Block (PCB)
 *
 **********************************************************************/

#define SVIEW_BASE   0xFC00
#define PIO_BASE     0xFD00
#define PCB_BASE     0xFF00


/**********************************************************************
 *
 *  Interrupt Map
 *
 **********************************************************************/

/*
 * Zilog 85230 SCC
 */
#define SCC_INT         17

/* 
 * On-Chip Timer/Counters
 */
#define TIMER0_INT       8
#define TIMER1_INT      18
#define TIMER2_INT      19

/* 
 * On-Chip Serial Ports
 */
#define RX_INT          20
#define TX_INT          21


/**********************************************************************
 *
 *  On-Chip Peripherals
 *
 **********************************************************************/

/* 
 * Interrupt Control Unit    
 */
#define EOI     (PCB_BASE + 0x02)
#define POLL    (PCB_BASE + 0x04)
#define POLLSTS (PCB_BASE + 0x06)

#define IMASK   (PCB_BASE + 0x08)
#define PRIMSK  (PCB_BASE + 0x0A)

#define INSERV  (PCB_BASE + 0x0C)
#define REQST   (PCB_BASE + 0x0E)
#define INSTS   (PCB_BASE + 0x10)

/* 
 * Timer/Counters 
 */
#define TCUCON  (PCB_BASE + 0x12)

#define T0CNT   (PCB_BASE + 0x30)
#define T0CMPA  (PCB_BASE + 0x32)
#define T0CMPB  (PCB_BASE + 0x34)
#define T0CON   (PCB_BASE + 0x36)

#define T1CNT   (PCB_BASE + 0x38)
#define T1CMPA  (PCB_BASE + 0x3A)
#define T1CMPB  (PCB_BASE + 0x3C)
#define T1CON   (PCB_BASE + 0x3E)

#define T2CNT   (PCB_BASE + 0x40)
#define T2CMPA  (PCB_BASE + 0x42)
#define T2CON   (PCB_BASE + 0x46)

/* 
 * Programmable I/O Ports         
 */
#define P1DIR   (PCB_BASE + 0x50)
#define P1PIN   (PCB_BASE + 0x52)
#define P1CON   (PCB_BASE + 0x54)
#define P1LTCH  (PCB_BASE + 0x56)

#define P2DIR   (PCB_BASE + 0x58)
#define P2PIN   (PCB_BASE + 0x5A)
#define P2CON   (PCB_BASE + 0x5C)
#define P2LTCH  (PCB_BASE + 0x5E)


/**********************************************************************
 *
 *  Clock Speeds
 *
 **********************************************************************/

#define SYS_CLOCK   25000000L        /* 25-MHz     */
#define SCC_CLOCK   12288000L        /* 12.288-MHz */


#endif /* _TGT188EB_H */
