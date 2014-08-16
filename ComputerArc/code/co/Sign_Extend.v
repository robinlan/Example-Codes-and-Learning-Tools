//Subject:     CO project 2 - Sign extend
//--------------------------------------------------------------------------------
//Version:     1
//--------------------------------------------------------------------------------
//Writer:      
//----------------------------------------------
//Date:        
//----------------------------------------------
//Description: 
//--------------------------------------------------------------------------------

module Sign_Extend(
    data_i,
    data_o
    );
               
//I/O ports
input   [16-1:0] data_i;
output  [32-1:0] data_o;

//Internal Signals
reg     [32-1:0] data_o;
wire [31:0] out;
//Sign extended


assign out[0]=data_i[0];
assign out[1]=data_i[1];
assign out[2]=data_i[2];
assign out[3]=data_i[3];
assign out[4]=data_i[4];
assign out[5]=data_i[5];
assign out[6]=data_i[6];
assign out[7]=data_i[7];
assign out[8]=data_i[8];
assign out[9]=data_i[9];
assign out[10]=data_i[10];
assign out[11]=data_i[11];
assign out[12]=data_i[12];
assign out[13]=data_i[13];
assign out[14]=data_i[14];
assign out[15]=data_i[15];

assign out[31:16]=(data_i[15]==1)?16'b1111111111111111 : 16'b0000000000000000;

always @(*) begin
	data_o<=out;
end

endmodule      
     
