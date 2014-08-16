//Subject:      CO project 2 - Shift_Left_Two_32
//--------------------------------------------------------------------------------
//Version:     1
//--------------------------------------------------------------------------------
//Description: 
//--------------------------------------------------------------------------------

module Shift_Left_Two_32(
    data_i,
    data_o
    );

//I/O ports                    
input [32-1:0] data_i;
output [32-1:0] data_o;

//shift left 2

assign data_o[0]=1'b0;
assign data_o[1]=1'b0;
assign data_o[2]=data_i[0];
assign data_o[3]=data_i[1];
assign data_o[4]=data_i[2];
assign data_o[5]=data_i[3];
assign data_o[6]=data_i[4];
assign data_o[7]=data_i[5];
assign data_o[8]=data_i[6];
assign data_o[9]=data_i[7];
assign data_o[10]=data_i[8];
assign data_o[11]=data_i[9];
assign data_o[12]=data_i[10];
assign data_o[13]=data_i[11];
assign data_o[14]=data_i[12];
assign data_o[15]=data_i[13];
assign data_o[16]=data_i[14];
assign data_o[17]=data_i[15];
assign data_o[18]=data_i[16];
assign data_o[19]=data_i[17];
assign data_o[20]=data_i[18];
assign data_o[21]=data_i[19];
assign data_o[22]=data_i[20];
assign data_o[23]=data_i[21];
assign data_o[24]=data_i[22];
assign data_o[25]=data_i[23];
assign data_o[26]=data_i[24];
assign data_o[27]=data_i[25];
assign data_o[28]=data_i[26];
assign data_o[29]=data_i[27];
assign data_o[30]=data_i[28];
assign data_o[31]=data_i[29];

endmodule
