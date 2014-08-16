/**
 * blink.cpp - blink test fabooh style
 *
 * Pins Used: RED_LED, GREEN_LED, PUSH2
 */

#include <fabooh.h>

#define SMALL_INIT4 /* don't bother to initialize .bss and .data sections we don't use either */
#include <main.h>

void setup() {
  if( is_same_port<RED_LED,GREEN_LED>::yes_no ) {
    port_mode(RED_LED,GREEN_LED,OUTPUT);
  }
  else {
    RED_LED::set_mode(OUTPUT);
    GREEN_LED::set_mode(OUTPUT);
  }
  PUSH2::set_mode(INPUT_PULLUP);

  RED_LED::high();
  GREEN_LED::low();
}

void loop() {
  // block loop if user holds down the button
  if ( PUSH2::is_pushed()) {
    do {
      delay_msecs(10); // debounce the button
    } while(PUSH2::is_pushed());
  }

  if ( is_same_port<RED_LED, GREEN_LED>::yes_no ) {
    port_toggle(RED_LED,GREEN_LED);
  }
  else {
    RED_LED::toggle();
    GREEN_LED::toggle();
  }

  delay_msecs(100);
}
