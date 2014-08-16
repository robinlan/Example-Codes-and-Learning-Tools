`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    22:17:20 12/22/2011 
// Design Name: 
// Module Name:    Hazerd 
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
module Hazerd(Branch_i,rs,rt,addr,MemRead,Branch_o,stall);

input Branch_i,MemRead;
input [4:0] rs,rt,addr;
output Branch_o,stall;
assign Branch_o=Branch_i;
assign stall=( (rs==addr || rt==addr) && MemRead==1)? 1:0;


endmodule
