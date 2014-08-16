// scanner.h

#ifndef _SCANNER_H
#define _SCANNER_H

/*
 * scanner tokens
 */
enum tokens_e {
  T_EOI = 256,

  T_INT16,
  T_INT32,
  T_FLOAT,
  T_OPERATOR,

  T_ADD = '+',
  T_SUB = '-',
  T_DIV = '/',
  T_MUL = '*',
  T_MODULO = '%',

  T_UNKNOWN = -1
};

/*
 * adjustable size scanner buffer
 */
template<const uint8_t SIZE=24>
struct scanner_state_t {
  char *start, *end;
  char inbuf[SIZE];
  char * endbuf;

  void init() {
    start = end = inbuf;
    endbuf = inbuf+SIZE-1; // leave room for the EOI
  }
};

typedef scanner_state_t<> scanner_state; // create typical use

struct scanner_token {
  tokens_e token;
  union {
    float f;
    unsigned long u32;
    long n32;
    unsigned u16;
    int n16;
  };
};

int scan(scanner_state &, scanner_token &);

#endif
