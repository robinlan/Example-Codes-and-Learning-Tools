//******************************************************************************
//  MSP430x54x Demo - Block Write and Memory Erase @ 0x10000 Executed from RAM
//
//  Description: This program first copies write_block_int function to RAM.
//  Copying to RAM requires a custom made .xcl command linker file. This file is
//  attached as part of the project called custom_lnk_msp430f5438.xcl. This
//  program then erases Flash address 0x10000 - 0x1FFFF which is executed from
//  RAM. Then, it writes to Bank 1 from 0x10000 to 0x1003F. This write shows the
//  advantage of block writing in which 2 consecutive word can be written.
//
//  Important Notes:
//
//  1. Copy a new linker file (custom_lnk_msp430f5438.xcl) before modifying it.
//     See the zip file for the modified linker file. 1 line was added and 2
//     line was changed. Goto Project Options --> Linker --> Config. Overide the
//     linker command file and insert the new .xcl file.
//
//     -QRAMCODE=FLASHCODE                   // Needed to tell compiler that user
//                                              will copy flash code to ram code
//     -Z(DATA)RAMCODE,DATA16_I,DATA16_Z,DATA16_N,DATA16_HEAP+_DATA16_HEAP_SIZE=1C00-5BFF
//                                           // Add RAMCODE into Flash segment
//     -P(CODE)FLASHCODE,CODE=5C00-FF7F,10000-45BFF
//                                           // Add FLASHCODE into Flash segment
//
//  2. Define the allocated memory area that will be copied from FLASH to RAM.
//     In this case, user has to manually define the start address of FLASH and
//     RAM. These memory addresses has to be the same as defined in the linker
//     file origin address of FLASH_MEM and RAM_MEM. The FLASH_MEM_LENGTH can be
//     changed to however much the final compiled code size is.
//
//     WRITE_LENGTH      EQU   64
//     FLASH_MEM_BEGIN   EQU   0x5C00       ; Flash code starting address
//     FLASH_MEM_LENGTH  EQU   0x0200       ; Function segment size to be copied
//     RAM_MEM_BEGIN     EQU   0x1C00       ; RAM code starting address
//     WRITE_ADDR        EQU   0x10000      ; Flash write address
//
//  RESET the device to re-execute code. This is implemented to prevent
//  stressing of Flash unintentionally.
//  ACLK = REFO = 32kHz, MCLK = SMCLK = default DCO 1048576Hz
//
//                MSP430x54x
//            -----------------
//        /|\|              XIN|-
//         | |                 |
//         --|RST          XOUT|-
//           |                 |
//
//   W. Goh
//   Texas Instruments Inc.
//   April 2009
//   Built with IAR Embedded Workbench Version: 4.11B
//******************************************************************************

#include "msp430x54x.h"
#include "string.h"

#pragma segment="FLASHCODE"                 // Define flash segment code
#pragma segment="RAMCODE"

// Function prototypes
void copy_flash_to_RAM(void);
void write_block_int(void);

unsigned long value;

void main(void)
{

  WDTCTL = WDTPW+WDTHOLD;                   // Stop WDT

  copy_flash_to_RAM();                      // Copy flash to RAM function

  value = 0x12340000;                       // initialize Value

  write_block_int();                        // This portion of code is executed
                                            // in RAM
  while(1);                                 // Loop forever, SET BREAKPOINT HERE
}

//------------------------------------------------------------------------------
// Copy flash function to RAM.
//------------------------------------------------------------------------------
void copy_flash_to_RAM(void)
{
  unsigned char *flash_start_ptr;           // Initialize pointers
  unsigned char *flash_end_ptr;
  unsigned char *RAM_start_ptr;

  //Initialize flash and ram start and end address
  flash_start_ptr = (unsigned char *)__segment_begin("FLASHCODE");
  flash_end_ptr = (unsigned char *)__segment_end("FLASHCODE");
  RAM_start_ptr = (unsigned char *)__segment_begin("RAMCODE");

  //calculate function size
  unsigned long function_size = (unsigned long)(flash_end_ptr) - (unsigned long)(flash_start_ptr);

  // Copy flash function to RAM
  memcpy(RAM_start_ptr,flash_start_ptr,function_size);
}

#pragma location="RAMCODE"
//------------------------------------------------------------------------------
// This portion of the code is first stored in Flash and copied to RAM then
// finally executes from RAM.
//-------------------------------------------------------------------------------
void write_block_int(void)
{
  unsigned int i;
  unsigned long * Flash_ptr;
  Flash_ptr = (unsigned long *)0x10000;     // Initialize write address
  __disable_interrupt();                    // 5xx Workaround: Disable global
                                            // interrupt while erasing. Re-Enable
                                            // GIE if needed
  // Erase Flash
  while(BUSY & FCTL3);                      // Check if Flash being used
  FCTL3 = FWKEY;                            // Clear Lock bit
  FCTL1 = FWKEY+ERASE;                      // Set Erase bit
  *Flash_ptr = 0;                           // Dummy write to erase Flash seg
  while(BUSY & FCTL3);                      // Check if Erase is done

  // Write Flash
  FCTL1 = FWKEY+BLKWRT+WRT;                 // Enable block write

  for(i = 0; i < 64; i++)
  {
    *Flash_ptr++ = value++;                 // Write long int to Flash

    while(!(WAIT & FCTL3));                 // Test wait until ready for next byte
  }

  FCTL1 = FWKEY;                            // Clear WRT, BLKWRT
  while(BUSY & FCTL3);                      // Check for write completion
  FCTL3 = FWKEY+LOCK;                       // Set LOCK
}