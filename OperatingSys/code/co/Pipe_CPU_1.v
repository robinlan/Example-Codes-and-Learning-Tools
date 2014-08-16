//Subject:     CO project 4 - Pipe CPU 1
//--------------------------------------------------------------------------------
//Version:     1
//--------------------------------------------------------------------------------
//Writer:      
//----------------------------------------------
//Date:        
//----------------------------------------------
//Description: 
//--------------------------------------------------------------------------------
module Pipe_CPU_1(
        clk_i,
		rst_i
		);
    
/****************************************
I/O ports
****************************************/
input clk_i;
input rst_i;

/****************************************
Internal signal
****************************************/
/**** IF stage ****/


/**** ID stage ****/

//control signal


/**** EX stage ****/

//control signal


/**** MEM stage ****/

//control signal


/**** WB stage ****/

//control signal


/****************************************
Instnatiate modules
****************************************/
//Instantiate the components in IF stage

wire [31:0] pcin,pcout,pcin0,pcin1;
wire pcsrc;
wire [31:0] instr;

MUX_2to1 #(.size(32)) PCMUX(pcin0,pcin1,pcsrc,pcin);

PC PC(clk_i,rst_i,pcin,pcout,stall);

Instr_Memory IM(pcout,instr);
			
Adder Add_pc(pcout,4,pcin0);


wire [64-1:0] IF_ID_i;
assign IF_ID_i[63:32]=pcin0;
assign IF_ID_i[31:0]=instr;
wire [64-1:0] IF_ID_o;
wire isBranch;	

Pipe_Reg #(.size(64)) IF_ID(       //N is the total length of input/output
clk_i,rst_i,IF_ID_i,IF_ID_o,isBranch,stall
		);
		
//Instantiate the components in ID stage


wire stall;

Hazerd hazard(pcsrc,rs,rt,Mux2_o,MemRead_EX,isBranch,stall);

wire [31:0] pcout_ID;
wire [31:0] instr_ID;
assign pcout_ID=IF_ID_o[63:32];
assign instr_ID=IF_ID_o[31:0];
wire [4:0] rs;
wire [4:0] rt;
wire [4:0] rd;
wire [5:0] op;
wire [15:0] imm;
wire [6:0] func;
assign op=instr_ID[31:26];
assign rs=instr_ID[25:21];
assign rt=instr_ID[20:16];
assign rd=instr_ID[15:11];
assign func=instr_ID[5:0];
assign imm=instr_ID[15:0];

wire Branch,MemToReg,MemRead,MemWrite,ALUSrc,RegWrite,Regdst;
wire [2:0] ALUOp;

wire [31:0] rsdata,rtdata;

Reg_File RF(
clk_i,rst_i,rs,rt,RegWriteAddr, RegWriteData  , RegWrite_WB ,rsdata,rtdata
		);

Decoder Control(
op,func,Branch,MemToReg,MemRead,MemWrite,ALUOp,ALUSrc,RegWrite,Regdst
		);

wire [31:0] imm32;
Sign_Extend Sign_Extend(
imm,imm32
		);

wire [152:0] ID_EX_i;

assign ID_EX_i[31:0]=rsdata;
assign ID_EX_i[63:32]=rtdata;
assign ID_EX_i[95:64]=imm32;
assign ID_EX_i[100:96]=rt;
assign ID_EX_i[105:101]=rd;
assign ID_EX_i[106]=(stall==1)?0:Branch;
assign ID_EX_i[107]=MemToReg;
assign ID_EX_i[108]=MemRead;
assign ID_EX_i[109]=(stall==1)?0:MemWrite;
assign ID_EX_i[110]=ALUSrc;
assign ID_EX_i[111]=(stall==1)?0:RegWrite;
assign ID_EX_i[112]=Regdst;
assign ID_EX_i[115:113]=ALUOp;
assign ID_EX_i[147:116]=pcout_ID;
assign ID_EX_i[152:148]=rs;
wire [152:0] ID_EX_o;

Pipe_Reg #(.size(153)) ID_EX(
clk_i,rst_i, ID_EX_i,ID_EX_o,isBranch,0
		);
		
//Instantiate the components in EX stage	   

wire [31:0] rsdata_EX;
wire [31:0] rtdata_EX;
wire [31:0] imm32_EX;
wire [4:0] rt_EX;
wire [4:0] rd_EX;
wire Branch_EX,MemToReg_EX,MemRead_EX,MemWrite_EX,ALUSrc_EX,RegWrite_EX,Regdst_EX;
wire [2:0] ALUOp_EX;
wire [31:0] pcout_EX;
wire [4:0] rs_EX;

assign rsdata_EX=ID_EX_o[31:0];
assign rtdata_EX=ID_EX_o[63:32];
assign imm32_EX=ID_EX_o[95:64];
assign rt_EX=ID_EX_o[100:96];
assign rd_EX=ID_EX_o[105:101];
assign Branch_EX=ID_EX_o[106];
assign MemToReg_EX=ID_EX_o[107];
assign MemRead_EX=ID_EX_o[108];
assign MemWrite_EX=ID_EX_o[109];
assign ALUSrc_EX=ID_EX_o[110];
assign RegWrite_EX=ID_EX_o[111];
assign Regdst_EX=ID_EX_o[112];
assign ALUOp_EX=ID_EX_o[115:113];
assign pcout_EX=ID_EX_o[147:116];
assign rs_EX=ID_EX_o[152:148];
wire [31:0] ALUResult;
wire zero;
wire [3:0] ALUCtrl;


wire [31:0] ALUdata0,ALUdata1;

MUX_4to1 #(.size(32)) MuxCtrl0(rsdata_EX,ALUResult_MEM,RegWriteData,0,ctrl0,ALUdata0);
MUX_4to1 #(.size(32)) MuxCtrl1(rtdata_EX,ALUResult_MEM,RegWriteData,0,ctrl1,ALUdata1);


ALU ALU(
ALUdata0,Mux1_o,ALUCtrl,ALUResult,zero
		);


ALU_Ctrl ALU_Ctrl(
imm32_EX[5:0], ALUOp_EX,ALUCtrl
		);

wire [31:0] Mux1_o;
MUX_2to1 #(.size(32)) Mux1(
ALUdata1,imm32_EX,ALUSrc_EX, Mux1_o
        );

wire [4:0] Mux2_o;		
MUX_2to1 #(.size(5)) Mux2(
rt_EX,rd_EX,Regdst_EX,Mux2_o
        );

wire [31:0] immS2;
Shift_Left_Two_32 SLT32(imm32_EX,immS2);

wire [31:0] pc_Branch;
Adder Add_Branch(pcout_EX,immS2,pc_Branch);

wire [1:0] ctrl0,ctrl1;

Forwarding FW(RegWrite_MEM,RegWrite_WB,rs_EX,rt_EX,WBaddr,RegWriteAddr,ctrl0,ctrl1);


wire [106:0] EX_MEM_i;
assign EX_MEM_i[31:0]=ALUResult;
assign EX_MEM_i[63:32]=ALUdata1;
assign EX_MEM_i[95:64]=pc_Branch;
assign EX_MEM_i[100:96]=Mux2_o;
assign EX_MEM_i[101]=zero;
assign EX_MEM_i[102]=Branch_EX;
assign EX_MEM_i[103]=MemToReg_EX;
assign EX_MEM_i[104]=MemRead_EX;
assign EX_MEM_i[105]=MemWrite_EX;
assign EX_MEM_i[106]=RegWrite_EX;

wire [106:0] EX_MEM_o;

Pipe_Reg #(.size(107)) EX_MEM(
clk_i,rst_i,EX_MEM_i,EX_MEM_o,isBranch,0
		);
		
			   
//Instantiate the components in MEM stage

wire [31:0] ALUResult_MEM;
wire [31:0] WriteData;
wire [4:0] WBaddr;
wire zero_MEM,Branch_MEM,MemToReg_MEM,MemRead_MEM,MemWrite_MEM,RegWrite_MEM;

assign ALUResult_MEM=EX_MEM_o[31:0];
assign WriteData=EX_MEM_o[63:32];
assign pcin1=EX_MEM_o[95:64];
assign WBaddr=EX_MEM_o[100:96];
assign zero_MEM=EX_MEM_o[101];
assign Branch_MEM=EX_MEM_o[102];
assign MemToReg_MEM=EX_MEM_o[103];
assign MemRead_MEM=EX_MEM_o[104];
assign MemWrite_MEM=EX_MEM_o[105];
assign RegWrite_MEM=EX_MEM_o[106];

assign pcsrc= Branch_MEM & zero_MEM;

wire [31:0] ReadData;

Data_Memory DM(
clk_i,ALUResult_MEM,WriteData,MemRead_MEM,MemWrite_MEM,ReadData
	    );

wire [70:0] MEM_WB_i;

assign MEM_WB_i[31:0]=ReadData;
assign MEM_WB_i[63:32]=ALUResult_MEM;
assign MEM_WB_i[68:64]=WBaddr;
assign MEM_WB_i[69]=MemToReg_MEM;
assign MEM_WB_i[70]=RegWrite_MEM;

wire [70:0] MEM_WB_o;
Pipe_Reg #(.size(71)) MEM_WB(
clk_i,rst_i,MEM_WB_i,MEM_WB_o,0,0    
		);

//Instantiate the components in WB stage

wire [31:0] ReadData_WB,ALUResult_WB;
wire [4:0] RegWriteAddr;
wire MemToReg_WB,RegWrite_WB;

assign ReadData_WB=MEM_WB_o[31:0];
assign ALUResult_WB=MEM_WB_o[63:32];
assign RegWriteAddr=MEM_WB_o[68:64];
assign MemToReg_WB=MEM_WB_o[69];
assign RegWrite_WB=MEM_WB_o[70];

wire [31:0] RegWriteData;

MUX_2to1 #(.size(32)) Mux3(
ReadData_WB,ALUResult_WB,MemToReg_WB,RegWriteData
        );

/****************************************
signal assignment
****************************************/	
endmodule

