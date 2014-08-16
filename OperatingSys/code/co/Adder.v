//Subject:     CO project 2 - Adder
//--------------------------------------------------------------------------------
//Version:     1
//--------------------------------------------------------------------------------
//Writer:      Luke
//----------------------------------------------
//Date:        2010/8/16
//----------------------------------------------
//Description: 
//--------------------------------------------------------------------------------

module Adder(
    src1_i,
	src2_i,
	sum_o
	);
     
//I/O ports
input  [32-1:0]  src1_i;
input  [32-1:0]	 src2_i;
output [32-1:0]	 sum_o;


//Internal Signals
wire    [32-1:0]	 sum_o;
wire z;
//Parameter
    
//Main function
ALU M(.src1_i(src1_i), .src2_i(src2_i), .ctrl_i(2), .result_o(sum_o), .zero_o(z));

endmodule





