/*
 * nokia5110_logo.cpp - demo xbmto5110
 *
 * Created: Feb-3-2012
 *  Author: rick@kimballsoftware.com
 *    Date: 02-28-2013
 * Version: 1.0.0
 *
 */

#include <fabooh.h>
#include <main.h>

#define USE_SPI 0
#include <drivers/nokia_5110.h>

#include "bitmaps/lp_logo.inc"

typedef P1_0 LCD_DC;
typedef P1_4 LCD_CE;
//typedef P1_5 SCLK; // varies depending on the board selected
typedef MOSI SDI;    // varies depending on the board selected
typedef P1_6 LCD_BACKLIGHT;

inline void init_spi(void) {
  LCD_CE::setmode_output();
  LCD_CE::high(); // active low, so this disables

#if defined(USE_SPI) && USE_SPI
  #define SPI_MODE_0 (UCCKPH)           /* CPOL=0 CPHA=0 */
  #define SPI_MODE_1 (0)                /* CPOL=0 CPHA=1 */
  #define SPI_MODE_2 (UCCKPL | UCCKPH)  /* CPOL=1 CPHA=0 */
  #define SPI_MODE_3 (UCCKPL)           /* CPOL=1 CPHA=1 */

  UCB0CTL1 = UCSWRST | UCSSEL_2;                  // Put USCI in reset mode, source USCI clock from SMCLK
  UCB0CTL0 = SPI_MODE_3 | UCMSB | UCSYNC | UCMST; // seems to work with either MODE 0 or MODE 3
  P1SEL |= SCLK::pin_mask | SDI::pin_mask;        // configure P1.5 and P1.7 for USCI
  P1SEL2 |= SCLK::pin_mask | SDI::pin_mask;       // more required SPI configuration
  UCB0BR0 = 8;                                    // set SPI clock to 2MHz ... SMCLK/8, (16000000/8)
  UCB0BR1 = 0;
  UCB0CTL1 &= ~UCSWRST;                           // release USCI for operation
#else
  SCLK::setmode_output();
  SDI::setmode_output();
  SCLK::high();
#endif
}

void setup() {
  LCD_DC::setmode_output();
  LCD_BACKLIGHT::setmode_output();
  LCD_DC::high();

  init_spi();

  lcd::nokia::Nokia5110<SCLK, SDI, LCD_DC, LCD_CE, 100> lcd;

  lcd.reset();
  lcd.init();
  lcd.clear();

  LCD_BACKLIGHT::high(); // turn off backlight

  do {
    lcd.clear();
    lcd.bitmap(lp_logo, (84 - 56) / 2, 0);
    LPM4;
  } while (1);
}

void loop() {}
