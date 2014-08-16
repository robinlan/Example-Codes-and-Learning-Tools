`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    22:35:08 12/22/2011 
// Design Name: 
// Module Name:    Forwarding 
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
module Forwarding(RegWrite_MEM,RegWrite_WB,rs,rt,rd_MEM,rd_WB,ctrl0,ctrl1);
input RegWrite_MEM,RegWrite_WB;
input [4:0] rs,rt,rd_MEM,rd_WB;
output [1:0] ctrl0,ctrl1;

reg [1:0] ctrl0,ctrl1;

always @(*) begin

if(RegWrite_MEM && rd_MEM!=0 && rs==rd_MEM ) 
	ctrl0<=1;
else if(RegWrite_WB && rd_WB!=0 && rs==rd_WB)
	ctrl0<=2;
else
	ctrl0<=0;

if(RegWrite_MEM && rd_MEM!=0 && rt==rd_MEM)
	ctrl1<=1;
else if(RegWrite_WB && rd_WB!=0 && rt==rd_WB)
	ctrl1<=2;
else
	ctrl1<=0;
end 


endmodule
