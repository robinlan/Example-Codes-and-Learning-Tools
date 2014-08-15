/**********************************************************************
 *
 * Filename:    crc.c
 * 
 * Description: A table-driven implementation of CRC-CCITT checksums.
 *
 * Notes:       Some of the constants in this file are specific to 
 *              Arcom's Target188EB hardware.
 *
 *              This code can be easily modified to implement any
 *              "non-reflective" CRC algorithm.  Simply change the
 *              constants POLYNOMIAL, INITIAL_REMAINDER, FINAL_XOR,
 *              and--if an 8 or 32-bit CRC is required--the definition
 *              of type 'width'.
 *
 * 
 * Copyright (c) 1998 by Michael Barr.  This software is placed into
 * the public domain and may be used for any purpose.  However, this
 * notice must not be changed or removed and no warranty is either
 * expressed or implied by its publication or distribution.
 **********************************************************************/

#include <string.h>

#include "led.h"


/* 
 * The CRC parameters.  Currently configured for CCITT.
 * Simply modify these to switch to another CRC standard.
 */
#define POLYNOMIAL          0x1021
#define INITIAL_REMAINDER   0xFFFF
#define FINAL_XOR_VALUE     0x0000

/*
 * The width of the CRC calculation and result.
 * Modify the typedef for an 8 or 32-bit CRC standard.
 */
typedef unsigned short width;

#define WIDTH   (8 * sizeof(width))
#define TOPBIT  (1 << (WIDTH - 1))


/*
 * An array containing the pre-computed intermediate result for each
 * possible byte of input.  This is used to speed up the computation.
 */
width  crcTable[256];


/**********************************************************************
 *
 * Function:    crcInit()
 *
 * Description: Initialize the CRC lookup table.  This table is used
 *              by crcCompute() to make CRC computation faster.
 *
 * Notes:       The mod-2 binary long division is implemented here.
 *
 * Returns:     None defined.
 *
 **********************************************************************/
void
crcInit(void)
{
    width  remainder;              
    width  dividend;              
    int    bit;                  


    /*
     * Perform binary long division, a bit at a time.
     */
    for (dividend = 0; dividend < 256; dividend++)
    {
        /*
         * Initialize the remainder.
         */
        remainder = dividend << (WIDTH - 8);

        /*
         * Shift and XOR with the polynomial.
         */
        for (bit = 0; bit < 8; bit++)
        {
            /*
             * Try to divide the current data bit.
             */
            if (remainder & TOPBIT)
            {
                remainder = (remainder << 1) ^ POLYNOMIAL;
            }
            else
            {
                remainder = remainder << 1;
            }
        }

        /*
         * Save the result in the table.
         */
        crcTable[dividend] = remainder;
    }

}   /* crcInit() */


/**********************************************************************
 *
 * Function:    crcCompute()
 *
 * Description: Compute the CRC checksum of a binary message block.
 *
 * Notes:       This function expects that crcInit() has been called
 *              first to initialize the CRC lookup table.
 *
 * Returns:     The CRC of the data.
 *
 **********************************************************************/
width
crcCompute(unsigned char * message, unsigned int nBytes)
{
    unsigned int   offset;
    unsigned char  byte;
    width          remainder = INITIAL_REMAINDER;


    /*
     * Divide the message by the polynomial, a byte at time.
     */
    for (offset = 0; offset < nBytes; offset++)
    {
        byte = (remainder >> (WIDTH - 8)) ^ message[offset];
        remainder = crcTable[byte] ^ (remainder << 8);
    } 

    /*
     * The final remainder is the CRC result.
     */
    return (remainder ^ FINAL_XOR_VALUE);

}   /* crcCompute() */


/**********************************************************************
 *
 * Function:    main()
 *
 * Description: Test the CRC functions by computing the CRC-CCITT of
 *              the check string "123456789".  The expected result
 *              was provided by an independent third-party.
 *
 * Notes:
 *
 * Returns:     0 on success.
 *              Otherwise -1 indicates failure.
 *
 **********************************************************************/
main()
{
    #define CCITT_CHECK 0x29B1

    char * s = "123456789";


    /*
     * Initialize the CRC lookup table.
     */
	crcInit();

    /*
     * Compute the CRC of the check string.
     */
    if (crcCompute(s, strlen(s)) != CCITT_CHECK)
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
