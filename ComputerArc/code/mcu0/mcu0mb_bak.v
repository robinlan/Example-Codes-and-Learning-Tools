`define W 15

module register(input load, input [`W:0] idata, output [`W:0] data);
  reg    [`W:0] data;
  initial data = 0;
  always @(load) begin
    if (load) data = idata;
  end
endmodule

module mux(input s, input [`W:0] i0, i1, output [`W:0] o);
  assign o = s?i1:i0;
endmodule

module adder(input [`W:0] a, input [`W:0] b, output [`W:0] y);
  assign y = a+b;
endmodule

module memory(input load, input [`W:0] addr, input [`W:0] idata, output [`W:0] odata);
  parameter size = 128;
  reg [7:0] m [0:size-1];
  always @(posedge load) begin
    if (addr >=0 && addr < size-1)
      {m[addr], m[addr+1], m[addr+2], m[addr+3]} = idata;
  end
  assign odata = {m[addr], m[addr+1], m[addr+2], m[addr+3]};
endmodule

module alu(input [`W:0] a, input [`W:0] b, input [3:0] op, output reg [`W:0] y);
  parameter [3:0] NOP=4'h0, ADD=4'h1, SUB=4'h2, MUL=4'h3, DIV=4'h4, AND=4'h5, OR=4'h6, XOR=4'h7, NOT=4'h8;
  always@(a or op) begin
    case(op)
      4'h0: y = a;
      4'h1: y = a + b;
      4'h2: y = a - b;
      4'h3: y = a * b;
      4'h4: y = a / b;
      4'h5: y = a & b;
      4'h6: y = a | b;
      4'h7: y = a ^ b;
      4'h8: y = ~a;   
    endcase
  end
endmodule

module controller(input [15:0] ir, output reg [3:0] alu_op, output reg jump);
  parameter [3:0] LD=4'h0,ADD=4'h1,JMP=4'h2,ST=4'h3,CMP=4'h4,JEQ=4'h5;
  always@(ir) begin
    case (ir[15:12])
      LD: begin alu_op = ALU.NOP; jump=0; end
      ADD:begin alu_op = ALU.ADD; jump=0; end
      JMP:begin alu_op = ALU.NOP; jump=1; end
      ST :begin alu_op = ALU.NOP; jump=0; end
      CMP:begin alu_op = ALU.SUB; jump=0; end
      JEQ:begin alu_op = ALU.NOP; 
//	if jump=1; 
        jump = 0;
      end
    endcase
  end
endmodule

module main;
reg clock;
wire [`W:0] pc, pc_i, alu_o, m, a, ir_i, ir, pc_next;
wire [3:0] alu_op;
integer i;  

mux      JMUX(jump, pc_next, {4'h0, ir[11:0]}, pc_i);
register PC(clock, pc_i, pc);
adder    ADDER(pc, 2, pc_next);
// adder    ADDER(pc, 2, pc_i); 
memory   IM(0, pc, 0, ir);
// register IR(clock, ir_i, ir);
memory   DM(0, {4'h0,ir[11:0]}, 0, m);
alu      ALU(m, a, alu_op, alu_o);
register A(clock, alu_o, a);
controller CTL(ir, alu_op, jump);

initial begin 
  clock = 0;
  CTL.jump  = 0;
//PC.data = 0;
  $readmemh("mcu0m.hex", IM.m);
  for (i=0; i < 32; i=i+2) begin
    $display("%8x: %8x", i, {IM.m[i], IM.m[i+1]});
  end  
  $readmemh("mcu0m.hex", DM.m);
  for (i=0; i < 32; i=i+2) begin
    $display("%8x: %8x", i, {DM.m[i], DM.m[i+1]});
  end  
  $monitor("%4dns pc=%x ir=%x m=%x a=%x", $stime, pc, ir, m, a);
  #1000 $finish;
end
always #5 begin
  clock=~clock;
end
endmodule


/*
`define N    SW[15] // 負號旗標
`define Z    SW[14] // 零旗標
`define OP   IR[15:12] // 運算碼
`define C    IR[11:0]  // 常數欄位
`define M    {m[`C], m[`C+1]}

module cpu(input clock); // CPU0-Mini 的快取版：cpu0mc 模組
  parameter [3:0] LD=4'h0,ADD=4'h1,JMP=4'h2,ST=4'h3,CMP=4'h4,JEQ=4'h5;
  reg signed [15:0] A;   // 宣告暫存器 R[0..15] 等 16 個 32 位元暫存器
  reg [15:0] IR;  // 指令暫存器 IR
  reg [15:0] SW;  // 指令暫存器 IR
  reg [15:0] PC;  // 程式計數器
  reg [15:0] pc;
  reg [7:0]  m [0:32];    // 內部的快取記憶體
  reg signed [15:0] M;
  integer i;  
  initial  // 初始化
  begin
    PC = 0; // 將 PC 設為起動位址 0
	SW = 0;
    $readmemh("cpu16m.hex", m);
    for (i=0; i < 32; i=i+2) begin
       $display("%8x: %8x", i, {m[i], m[i+1]});
    end
  end
  
  always @(posedge clock) begin // 在 clock 時脈的正邊緣時觸發
      IR = {m[PC], m[PC+1]};  // 指令擷取階段：IR=m[PC], 2 個 Byte 的記憶體
	  pc = PC;
      PC = PC+2;              // 擷取完成，PC 前進到下一個指令位址
	  M = {m[`C], m[`C+1]};
      case (`OP)
        LD: A = M; 
        ST: {m[`C], m[`C+1]} = A;
        CMP: begin `N=(A < M); `Z=(A==M); end
        ADD: A = A + M;
        JMP: PC = `C;
        JEQ: if (`Z) PC=`C;
      endcase
      $display("%4dns PC=%x IR=%x, SW=%x, A=%d", $stime, pc, IR, SW, A);
  end
endmodule

module main;                // 測試程式開始
reg clock;                  // 時脈 clock 變數

cpu cpux(clock);            // 宣告 cpu0mc 處理器

initial clock = 0;          // 一開始 clock 設定為 0
always #10 clock=~clock;    // 每隔 10 奈秒將 clock 反相，產生週期為 20 奈秒的時脈
initial #2000 $finish;      // 在 640 奈秒的時候停止測試。(因為這時的 R[1] 恰好是 1+2+...+10=55 的結果)
endmodule
*/