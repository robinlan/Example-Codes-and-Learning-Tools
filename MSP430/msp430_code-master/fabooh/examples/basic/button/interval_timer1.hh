/*
 * interval_timer1.hh - simple Timer1 interval timer
 *
 * Created: Mar 25, 2013
 *  Author: rick@kimballsoftware.com
 *    Date: 03-16-2013
 * Version: 1.0.1
 *
 */

#ifndef INTERVAL_TIMER1_HH_
#define INTERVAL_TIMER1_HH_

#include <fabooh.h>

extern volatile unsigned long systicks;

/*
 * IntervalTimer1 - template for a 1 msec periodic timer
 *
 * looks for a user_handler_t that has a implements "static void on_tick(uint32_t)"
 */
template<typename USER_HANDLER_T, const unsigned interval_cycles=CPU::msec_cycles>
struct IntervalTimer1 {
  static USER_HANDLER_T _usercode;
  static USER_HANDLER_T & usercode(void) { return _usercode; }

  __attribute__ ((interrupt(TIMER1_A0_VECTOR)))
  static void on_interrupt(void)
  {
    TA1CCR0 += interval_cycles;   // setup next interrupt time
    systicks++;

    usercode().on_tick(systicks);    // call user function
  }

  static void init(void)
  {
    TA1CCTL0 = CCIE;
    TA1CCR0 = interval_cycles;    // next interrupt time
    TA1CTL = TASSEL_2 | MC_2;     // use SMCLK, Continuous Up Mode

    // reference the ISR so it isn't garbage collected by the linker
    (void)IntervalTimer1<USER_HANDLER_T>::on_interrupt;
  }
};


#endif /* INTERVAL_TIMER1_HH_ */
