/**
 * ascii_table_extended.ino - ASCII table print using insertion operator style
 *
 * Author: rick@kimballsoftware.com
 * Date: 03-03-2013
 *
 * $ msp430-size ascii_table_extended.elf
 *   text    data     bss     dec     hex filename
 *    500       2       0     502     1f6 ascii_table_extended.elf
 *
 */
#include <fabooh.h>
#include <serial.h>
#include <main.h>

//------- File Globals ------
namespace {
  const uint32_t BAUD_RATE=9600;
  typedef serial_default_t<BAUD_RATE, CPU::frequency, TX_PIN, NO_PIN> serial;

  serial Serial;
  unsigned thisByte=' '; // first visible ASCIIcharacter ' ' is number 32:
}

inline void setup(void)
{
  // initialize serial port pins.
  // Note: BAUD_RATE is ignored by begin. It is provided just
  // for informational purposes. You should change the baud
  // rate in the template definition
  Serial.begin(BAUD_RATE);

  // prints title with ending line break
  Serial << "ASCII Table ~ Character Map" << endl;
}

void loop()
{
  Serial  << _RAW(thisByte)
          << ", dec: " << thisByte
          << ", oct: 0" << _OCT(thisByte)
          << ", hex: 0x" << _HEX(thisByte)
          << ", bin: 0b" << _BIN(thisByte)
          << endl;

  // if printed last visible character '~' or 126, stop:
  if( thisByte == '~') {
    LPM4;
  }

  // go on to the next character
  thisByte++;
}
