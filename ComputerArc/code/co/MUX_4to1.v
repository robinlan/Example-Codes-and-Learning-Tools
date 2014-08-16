`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    00:40:25 12/23/2011 
// Design Name: 
// Module Name:    MUX_4to1 
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
module MUX_4to1(data0,data1,data2,data3,select,data_o);
parameter size = 0;
input [size-1:0] data0,data1,data2,data3;
input [1:0] select;
output [size-1:0] data_o;
wire s0,s1,s2;

wire [size-1:0] data_tmp0,data_tmp1;

assign s0=~select[1] & select[0];
assign s1=select[1] & select[0];
assign s2=( select[1] & ~select[0]) | (select[1] & select[0]);

MUX_2to1 #(.size(size)) M0(data0,data1,s0,data_tmp0);
MUX_2to1 #(.size(size)) M1(data2,data3,s1,data_tmp1);
MUX_2to1 #(.size(size)) M2(data_tmp0,data_tmp1,s2,data_o);

endmodule
