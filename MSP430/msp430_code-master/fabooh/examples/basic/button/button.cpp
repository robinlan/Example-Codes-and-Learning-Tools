/**
 * button.cpp - button test fabooh style
 *
 * Pins Used: RED_LED, GREEN_LED, PUSH2
 */

#include <fabooh.h>
#include <main.h>
#include <serial.h>
#include "interval_timer1.hh"

volatile unsigned long systicks;

serial_default_t<9600, CPU::MCLK_FREQ,TX_PIN, NO_PIN> Serial;

/*
 * simple app - waits for button press, times how long it is held down
 *              toggles red led on and off, green led toggled every 1 msec
 *
 * on_tick() is called by IntervalTimer1 class every interval. This
 *   function samples the state of the push button waiting for it to
 *   be fully on or fully off.  A digital filter is used to debounce
 *   the button input.
 */

enum states { waiting_for_button, push2_down, push2_up };

struct app {
public:
  static const unsigned on_mask = 0x01ff; // 9 samples on to be fully on

  ALWAYS_INLINE
  static volatile states & get_state(void) {
    static volatile states app_state;
    return app_state;
  }

  ALWAYS_INLINE
  static void set_state(states rhs) {
    get_state() = rhs;
  }

  ALWAYS_INLINE
  static unsigned & press_duration(void) {
    static unsigned duration;
    return duration;
  }

  /*
   * poll the button looking for a persistent on or off pattern
   *
   * see: http://www.ganssle.com/debouncing-pt2.htm
   */
  ALWAYS_INLINE
  void on_tick(volatile unsigned long ticks) {
    static volatile unsigned button_sample;
    static volatile unsigned long tick_at_push;

    // sample button every 2 msec
    if ((ticks % 2) == 0) {
      GREEN_LED::toggle();

      unsigned sample = ((button_sample << 1) | (PUSH2::is_pushed() ? 1 : 0));

      states state = get_state();

      switch(state) {
      case waiting_for_button:
        if (sample == on_mask) { // did we get a solid on?
          tick_at_push = ticks;
          set_state(push2_down); // new state wake up main routine
          __bic_status_register_on_exit(LPM0_bits);
        }
        break;

      case push2_down:
        if ( sample == 0 ) {  // did we get a solid off?
          press_duration() = ticks-tick_at_push;
          set_state(push2_up); // new state wake up main routine
          __bic_status_register_on_exit(LPM0_bits);
        }
        break;

      default:
        break;
      }

      button_sample = on_mask & sample;
    }
  }

  /*
   * event_loop() - app sleeps except when state changes
   */
  ALWAYS_INLINE
  void event_loop() {
    LPM0; // wait for a state transition

    switch(app::get_state()) {
    case push2_down:
      Serial << "button down" << endl;
      RED_LED::toggle();
      break;

    case push2_up:
      Serial << "button up, pushed for " << app::press_duration() << " msecs" << endl;
      app::set_state(waiting_for_button);
      break;
    }
  }

};

typedef IntervalTimer1<app> ticktock;

void setup() {
  Serial.begin();
  RED_LED::set_mode(OUTPUT);
  GREEN_LED::set_mode(OUTPUT);
  PUSH2::set_mode(INPUT_PULLUP);

  RED_LED::low();
  GREEN_LED::low();

  ticktock::init();

  __enable_interrupt();
}

void loop() {
  ticktock::usercode().event_loop();
}
