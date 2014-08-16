/**
 * calc.cpp - simple serial integer only calculator
 *
 * Desc: Implements a serial calculator using the re2c tool to scan the
 *       input stream and turn it into tokens.  Calculator implementation
 *       left as an exercise. Only implements:  1+2=3
 *
 * $ msp430-size calc.elf * on the msp430g2231 w/serial_sw_uart
 *  text     data     bss     dec     hex filename
 *  1628        0       2    1630     65e calc.elf
 *
 */

#include <fabooh.h>
#include <main.h>

#include <serial.h>
#include "scanner.h"

namespace {
  const uint32_t BAUD_RATE = 9600;
  serial_default_t<BAUD_RATE, CPU::frequency, TX_PIN, RX_PIN > Serial; // TX=varies, RX=varies

  const char *prompt = "\r\nexpr> ";
}

void showtoken(scanner_token &); // forward declaration

void blink() {
  unsigned cnt = 6;
  do {
    RED_LED::toggle();
    delay(100);
  } while(--cnt);

  RED_LED::low();
}

void setup() {
  Serial.begin(BAUD_RATE);
  RED_LED::setmode_output();
  RED_LED::low();
}


ALWAYS_INLINE
void parse_expr() {
  scanner_state scanner; // use default 24 byte buffer
  scanner.init();

  /*
   * get a line of input from the serial port with echo and delete support
   */
  do {
    int c = Serial.read();

    if ( c == 127 ) { // delete key
      if ( scanner.start > scanner.inbuf ) {
        scanner.start--;
        Serial.write(c);
      }
      continue;
    }

    if ( c == '\r' || c == '=' ) { // end of input
      *scanner.start = c;
      break;
    }

    if ( scanner.start < scanner.endbuf ) { // are we going to exceed the buffer?
      *(scanner.start++) = c;
      Serial.write(c);
    }
    else {
      blink(); //  ring the bell, they exceeded the buffer size
    }

  } while(1);


  scanner.end = scanner.start;
  scanner.start = scanner.inbuf;

  const int MAX_TOKENS=6; /* 2+4 bytes 36 bytes */

  scanner_token expr_stack[MAX_TOKENS], *tok_ptr;
  unsigned token_type;

  tok_ptr = expr_stack; // point at first expression in array

  do {
    token_type = scan(scanner, *tok_ptr);
    showtoken(*tok_ptr);
    if (tok_ptr + 1 < &expr_stack[MAX_TOKENS]) {
      tok_ptr++; // just overwrite the last tok_values if they provide too many
    }
    else {
      blink();
    }
  } while (token_type != T_EOI);

  // OK, I parsed the string into tokens for you. I'll leave
  // the calculator implementation to you

  // To get you started, the sample below can deal with 1+2=3

  if ( expr_stack[0].token == T_INT16 &&
       expr_stack[1].token == T_OPERATOR &&
       expr_stack[2].token == T_INT16 ) {
    long n0 = expr_stack[0].n16, n1=expr_stack[2].n16;

    switch(expr_stack[1].n16) {
    case '+': n0 += n1; break;
    case '-': n0 -= n1; break;
    case '/': n0 /= n1; break;
    case '*': n0 *= n1; break;
    case '%': n0 %= n1; break;
    default: break;
    }

    // repeat back what they told us
    Serial << endl << expr_stack[0].u16 << _RAW(expr_stack[1].n16) << n1 << "=" << n0 << endl;
  }
  else {
    Serial << endl << "error!\n";
  }
}

/*
 * calculator implementation using re2c parser
 */
void loop() {
  do {
    Serial.print(prompt);
    parse_expr();
  } while(1);
}

/*
 * debug helper turns unprintable to '.'
 */
uint8_t guard_raw(unsigned c) {
  if ( c >= ' ' && c < 127 ) {
    return c;
  }
  else {
    return '.';
  }
}

/*
 * debug output to show the results of parser the input stream into tokens
 */
void showtoken(scanner_token &tok_value) {
#if 0
    Serial << "\ntok_value.token=0x" << _HEX(tok_value.token);

    if ( tok_value.token == T_INT16 ) {
      Serial << ", .n16=" << tok_value.n16 << endl;
    }
    else if ( tok_value.token == T_INT32 ) {
      Serial << ", .n32=" << tok_value.n32 << endl;
    }
    else {
      Serial  << ", .token='"<< _RAW(guard_raw(tok_value.token)) << "'"
              << ", .n16=0x" << _HEX(tok_value.n16)
              << ", .n16='" << _RAW(guard_raw(tok_value.n16)) << "'"
              << endl;
    }
#endif
}
