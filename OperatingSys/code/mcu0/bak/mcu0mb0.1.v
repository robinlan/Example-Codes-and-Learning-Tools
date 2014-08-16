`define W  16
`define W1 W-1

module register(input load, input [`W1:0] idata, output [`W1:0] odata);
  reg    [`W1:0] data;
  always @(posedge load) begin
    data = idata;
  end
  assign odata = data;
endmodule

module adder(input [`W1:0] a, input [`W1:0] b, output [`W1:0] y);
  assign y = a+b;
endmodule

module memory(input load, input [`W1:0] addr, input [`W1:0] idata, output [`W1:0] odata);
  reg [7:0] m [0:128];
  always @(posedge load) begin
    if (addr >=0 && addr < 128)
      {m[addr], m[addr+1], m[addr+2], m[addr+3]} = idata;
  end
  assign odata = {m[addr], m[addr+1], m[addr+2], m[addr+3]};
endmodule

module main;
reg clock;
wire [`W1:0] pc_o, pc_i;
wire [`W1:0] ir;
wire [`W1:0] M;
integer i;  

register pc(clock, pc_i, pc_o);
adder a(pc_o, 2, pc_i);
memory im(0, pc_o, 0, ir);
memory dm(0, {4'h0,ir[11:0]}, 0, M);

initial begin 
  clock = 0;
  pc.data = 0;
  $readmemh("mcu0m.hex", im.m);
  for (i=0; i < 32; i=i+2) begin
    $display("%8x: %8x", i, {im.m[i], im.m[i+1]});
  end  
  $readmemh("mcu0m.hex", dm.m);
  for (i=0; i < 32; i=i+2) begin
    $display("%8x: %8x", i, {dm.m[i], dm.m[i+1]});
  end  
  $monitor("%4dns clock=%d pc_o=%d ir=%x M=%x", $stime, clock, pc_o, ir, M);
  #500 $finish;
end
always #10 begin
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