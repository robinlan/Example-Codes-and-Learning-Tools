/**
 * hello_world.cpp - smallest serial print
 *
 * connect using putty/hyperterm/minicom .. 9600-8-N-1
 *
 * $ msp430-size hello_world.elf
 *    text    data     bss     dec     hex filename
 *     150       0       0     150      96 hello_world.elf
 *
 */

#include <fabooh.h>
#define SMALL_INIT4
#include <main.h>
#include <serial.h>

namespace {
  const uint32_t BAUD_RATE = 9600;
  serial_default_t<BAUD_RATE, CPU::frequency, TX_PIN, RX_PIN> Serial; // TX=varies, RX=varies
}

inline void setup() {
  Serial.begin(BAUD_RATE);

  Serial << "Hello world!\n";
}

inline void loop() {
  // do nothing;
}
