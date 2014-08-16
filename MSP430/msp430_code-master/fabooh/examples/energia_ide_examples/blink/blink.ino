/**
 * blink.ino - blink test arduino style
 *
 * Pins Used: RED_LED, GREEN_LED, PUSH2
 */

#include <fabooh.h>

#define SMALL_INIT4 /* don't initialize .bss and .data sections */
#include <main.h>

void setup() {
  pinMode(RED_LED,OUTPUT);
  pinMode(GREEN_LED,OUTPUT);
  pinMode(PUSH2,INPUT_PULLUP);

  digitalWrite(GREEN_LED, LOW);
  digitalWrite(RED_LED, HIGH);
}

void loop() {
  // block loop if user holds down the button
  if ( !digitalRead(PUSH2) ) {
    do {
      delay_msecs(10); // debounce switch
    } while(!digitalRead(PUSH2));
  }

  RED_LED::toggle();
  GREEN_LED::toggle();

  delay_msecs(500);
}
