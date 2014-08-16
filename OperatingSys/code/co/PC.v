`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    16:00:09 11/15/2011 
// Design Name: 
// Module Name:    PC 
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
module PC(clk,rst,pcin,pcout,stall);
input clk,rst;
input [31:0] pcin;
output [31:0] pcout;

input stall;

reg [31:0] pcout;

always @(posedge clk) begin
	if(~rst) pcout<=0;
	else if(stall==1) pcout<=pcout;
	else pcout<=pcin;
end


endmodule
