`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    22:22:33 11/16/2011 
// Design Name: 
// Module Name:    ALU_Ctrl 
// Project Name: 
// Target Devices: 
// Tool versions: 
// Description: 
//
// Dependencies: 
//
// Revision: 
// Revision 0.01 - File Created
// Additional Comments: 
//
//////////////////////////////////////////////////////////////////////////////////
module ALU_Ctrl(
          funct_i,
          ALUOp_i,
          ALUCtrl_o
          );
//I/O ports 
input      [6-1:0] funct_i;
input      [3-1:0] ALUOp_i;

output     [4-1:0] ALUCtrl_o;    
     
//Internal Signals
reg        [4-1:0] ALUCtrl_o;



always @(*) begin
	case(ALUOp_i)
		0:ALUCtrl_o<=2;
		1:ALUCtrl_o<=6;
		2:begin
			case(funct_i)
				32: ALUCtrl_o<=2;
				34: ALUCtrl_o<=6;
				36: ALUCtrl_o<=0;
				37: ALUCtrl_o<=1;
				42: ALUCtrl_o<=7;
				24: ALUCtrl_o<=3;
				8:  ALUCtrl_o<=2;
			endcase
		end
		3: ALUCtrl_o<=7;
		4: ALUCtrl_o<=9;  //bgez;
	endcase
end

endmodule
