clear all			% 清除所有工作空間的變數
x_double = magic(10);
x_single = single(x_double);
x32 = uint32(x_double);
x16 = uint16(x_double);
x8 = uint8(x_double);
whos