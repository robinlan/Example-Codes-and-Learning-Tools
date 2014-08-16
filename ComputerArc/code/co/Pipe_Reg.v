//Subject:     CO project 4 - Pipe Register
//--------------------------------------------------------------------------------
//Version:     1
//--------------------------------------------------------------------------------
//Writer:      
//----------------------------------------------
//Date:        
//----------------------------------------------
//Description: 
//--------------------------------------------------------------------------------
module Pipe_Reg(
                    clk_i,
						  rst_i,
					data_i,
					data_o,
					zero,
					stall
					);
					
parameter size = 0;

input                    clk_i,rst_i;		  
input      [size-1: 0] data_i;
output     [size-1: 0] data_o;
reg     [size-1: 0] data_o;


input zero;
input stall;
	  
always @(posedge clk_i) begin
	 if(~rst_i) data_o<=0;
	 else if(zero==1) data_o<=0;
	 else if(stall==1) data_o<=data_o;
    else data_o <= data_i;
end

endmodule	