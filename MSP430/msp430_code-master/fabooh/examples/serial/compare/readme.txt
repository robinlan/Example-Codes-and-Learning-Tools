readme.txt - compare straight c (opossum) with c++ (notopossum) sizes.
             At least on msp430-gcc it turns out the c++ is smaller

$ msp430-size opossum.elf 
 text    data     bss     dec     hex filename
  578       0       0     578     242 opossum.elf

$ msp430-size notopossum.elf 
 text    data     bss     dec     hex filename
  494       0       0     494     1ee notopossum.elf

