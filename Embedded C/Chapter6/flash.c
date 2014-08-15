/**********************************************************************
 *
 * Filename:    flash.c
 * 
 * Description: A software interface to the AMD 29F010 flash memory.
 *
 * Notes:       Some of the constants in this file are specific to 
 *              Arcom's Target188EB hardware.
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#include <string.h>

#include "tgt188eb.h"
#include "led.h"


/*
 * Features of the AMD 29F010 flash memory device.
 */
#define FLASH_SIZE              0x20000
#define FLASH_BLOCK_SIZE        0x04000

#define UNLOCK1_OFFSET          0x5555
#define UNLOCK2_OFFSET          0x2AAA
#define COMMAND_OFFSET          0x5555

#define FLASH_CMD_UNLOCK1       0xAA
#define FLASH_CMD_UNLOCK2       0x55
#define FLASH_CMD_READ_RESET    0xF0
#define FLASH_CMD_AUTOSELECT    0x90
#define FLASH_CMD_BYTE_PROGRAM  0xA0
#define FLASH_CMD_ERASE_SETUP   0x80
#define FLASH_CMD_CHIP_ERASE    0x10
#define FLASH_CMD_SECTOR_ERASE  0x30

#define DQ7    0x80
#define DQ5    0x20


/**********************************************************************
 *
 * Function:    flashWrite()
 *
 * Description: Write data to consecutive locations in the flash.
 *
 * Notes:       This function is specific to the AMD 29F010 Flash
 *              memory.  In that device, a byte that has been
 *              previously written must be erased before it can be
 *              rewritten successfully.
 *
 * Returns:     The number of bytes successfully written.  
 *
 **********************************************************************/
int
flashWrite(unsigned char *      baseAddress,
           const unsigned char  data[], 
           unsigned int         nBytes)
{
    unsigned char * flashBase = FLASH_BASE;
    unsigned int    offset;


    for (offset = 0; offset < nBytes; offset++)
    {
        /*
         * Issue the command sequence for byte program.
         */
        flashBase[UNLOCK1_OFFSET] = FLASH_CMD_UNLOCK1;
        flashBase[UNLOCK2_OFFSET] = FLASH_CMD_UNLOCK2;
        flashBase[COMMAND_OFFSET] = FLASH_CMD_BYTE_PROGRAM;

        /*
         * Perform the actual write operation.
         */
        baseAddress[offset] = data[offset];
		
        /*
         * Wait for the operation to complete or time-out.
         */
        while (((baseAddress[offset] & DQ7) != (data[offset] & DQ7)) && 
               !(baseAddress[offset] & DQ5));

        if ((baseAddress[offset] & DQ7) != (data[offset] & DQ7))
        {
            break;
        }
    }

    return (offset);                          

}   /* flashWrite() */


/**********************************************************************
 *
 * Function:    flashErase()
 *
 * Description: Erase a block of the flash memory device.
 *
 * Notes:       This function is specific to the AMD 29F010 flash
 *              memory.  In this device, individual sectors may be
 *              hardware protected.  If this algorithm encounters
 *              a protected sector, the erase operation will fail
 *              without notice.
 *
 * Returns:     O on success.  
 *              Otherwise -1 indicates failure.
 *
 **********************************************************************/
int
flashErase(unsigned char * sectorAddress)
{
    unsigned char * flashBase = FLASH_BASE;


    /*
     * Issue the command sequence for sector erase.
     */
    flashBase[UNLOCK1_OFFSET] = FLASH_CMD_UNLOCK1;
    flashBase[UNLOCK2_OFFSET] = FLASH_CMD_UNLOCK2;
    flashBase[COMMAND_OFFSET] = FLASH_CMD_ERASE_SETUP;
    flashBase[UNLOCK1_OFFSET] = FLASH_CMD_UNLOCK1;
    flashBase[UNLOCK2_OFFSET] = FLASH_CMD_UNLOCK2;

    *sectorAddress = FLASH_CMD_SECTOR_ERASE;

    /*
     * Wait for the operation to complete or time-out.
     */
    while (!(*sectorAddress & DQ7) && !(*sectorAddress & DQ5));

    if (!(*sectorAddress & DQ7))
    {
        return (-1);
    }

    return (0);
 
}   /* flashErase() */


/**********************************************************************
 *
 * Function:    main()
 *
 * Description: Minimalistic test code for the flash driver code.
 *
 * Notes:       It would be a very bad idea to erase and/or overwrite
 *              the first couple of sectors in the flash device.  The
 *              debug monitor code resides there!
 *
 * Returns:     0 on success.
 *              Otherwise -1 indicates failure.
 *
 **********************************************************************/
main(void)
{
    unsigned char * p = (unsigned char *) 0xD0000000;
    unsigned char * s = "Hello, World!";


    flashErase(p);
    flashWrite(p, s, strlen(s) + 1);

    if (strcmp(p, s))
    {
        toggleLed(LED_RED);
 
        return (-1);
    }
    else
    {
        toggleLed(LED_GREEN);
   
        return (0);
    }

}   /* main() */
