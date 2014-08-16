/*
 * bcdutils.h - convert from decimal to bcd routines
 *
 *  Author: kimballr
 *  Date: Feb 21, 2013
 *  Version: 1.00
 */

#ifndef BCDUTILS_H_
#define BCDUTILS_H_

#ifdef __cplusplus
extern "C" {
#endif

#define BCD_D0 0x0000000F /* 1     */
#define BCD_D1 0x000000F0 /* 10    */
#define BCD_D2 0x00000F00 /* 100   */
#define BCD_D3 0x0000F000 /* 1000  */
#define BCD_D4 0x000F0000 /* 10000 */

struct bcd16_t {
  uint8_t d0:4;
  uint8_t d1:4;
  uint8_t d2:4;
  uint8_t d3:4;
  uint8_t d4:4;
  uint8_t r5:4;  /* reserved */
  uint8_t r6:4;  /* reserved */
  uint8_t r7:4;  /* reserved */
};

struct bcd32_t {
  uint8_t d0:4;
  uint8_t d1:4;
  uint8_t d2:4;
  uint8_t d3:4;
  uint8_t d4:4;
  uint8_t d5:4;
  uint8_t d6:4;
  uint8_t d7:4;
  uint8_t d8:4;
  uint8_t d9:4;
  uint8_t sign:4;
  uint8_t rsvd11:4;
  uint8_t rsvd12:4;
  uint8_t rsvd13:4;
  uint8_t rsvd14:4;
  uint8_t rsvd15:4;
};

long u16tobcd(unsigned u16);
long long u32tobcd(unsigned long u32);

#ifdef __cplusplus
}
#endif

#endif /* BCDUTILS_H_ */
