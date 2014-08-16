`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    07:20:43 11/16/2011 
// Design Name: 
// Module Name:    Decoder 
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
module Decoder(
    instr,
    instr2,
	Branch,
	MemToReg,
	MemRead,
	MemWrite,
	ALUOp,
	ALUSrc,
	RegWrite,
	RegDst
	);
input [6-1:0] instr;
input [6-1:0] instr2;
output [3-1:0] ALUOp;
output Branch;
output MemToReg;
output MemRead;
output MemWrite;
output ALUSrc;
output RegWrite;
output RegDst;


reg [3-1:0] ALUOp;
reg Branch;
reg MemToReg;
reg MemRead;
reg MemWrite;
reg ALUSrc;
reg RegWrite;
reg RegDst;

always @(*) begin
	case(instr)
		//R-Type
		0: begin
			if(instr2==0) begin
				Branch<=0;
				MemToReg<=0;
				MemRead<=0;
				MemWrite<=0;
				ALUOp<=0;
				ALUSrc<=0;
				RegWrite<=0;
				RegDst<=0;
			end
			else begin
				Branch<=0;
				MemToReg<=1;
				MemRead<=0;
				MemWrite<=0;
				ALUOp<=2;
				ALUSrc<=0;
				RegWrite<=1;
				RegDst<=1;
			end
		end
		//ADDI
		8: begin
			Branch<=0;
			MemToReg<=1;
			MemRead<=0;
			MemWrite<=0;
			ALUOp<=0;
			ALUSrc<=1;
			RegWrite<=1;
			RegDst<=0;
		end
		//SLTI
		10: begin
			Branch<=0;
			MemToReg<=1;
			MemRead<=0;
			MemWrite<=0;
			ALUOp<=3;
			ALUSrc<=1;
			RegWrite<=1;
			RegDst<=0;
		end
		
		//lw
		35:begin
			Branch<=0;
			MemToReg<=0;
			MemRead<=1;
			MemWrite<=0;
			ALUOp<=0;
			ALUSrc<=1;
			RegWrite<=1;
			RegDst<=0;
		end
		//sw
		43: begin
			Branch<=0;
			MemToReg<=0;
			MemRead<=0;
			MemWrite<=1;
			ALUOp<=0;
			ALUSrc<=1;
			RegWrite<=0;
			RegDst<=0;
		end
		//BEQ
		4: begin
			Branch<=1;
			MemToReg<=0;
			MemRead<=0;
			MemWrite<=0;
			ALUOp<=1;
			ALUSrc<=0;
			RegWrite<=0;
			RegDst<=0;
		end
		
	endcase
end

endmodule
