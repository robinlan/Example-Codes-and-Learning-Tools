`define OP   IR[15:12] // 運算碼
`define C    IR[11:0]  // 常數欄位
`define C3   IR[2:0]   // 常數欄位
`define A    R[0]      // 累積器
`define LR   R[1]      // 狀態暫存器
`define SW   R[2]      // 狀態暫存器
`define SP   R[3]      // 堆疊暫存器
`define PC   R[4]      // 程式計數器
`define N    `SW[15]   // 負號旗標
`define Z    `SW[14]   // 零旗標
`define M    {m[`C], m[`C+1]} // 存取的記憶體

module cpu(input clock, input interrupt, input[2:0] irq); // CPU0-Mini 的快取版：cpu0mc 模組
  parameter [3:0] LD=4'h0,ADD=4'h1,JMP=4'h2,ST=4'h3,CMP=4'h4,JEQ=4'h5, SWI=4'h6, IRET=4'h7, LDS=4'h8, PUSH=4'h9, POP=4'hA;
  reg [15:0] IR;    // 指令暫存器
  reg [15:0] R[0:4];
  reg [15:0] pc0;
  reg [7:0]  m [0:128];    // 內部的快取記憶體
  reg isInterrupted;
  integer i;  
  initial  // 初始化
  begin
    `PC = 0; // 將 PC 設為起動位址 0
    `SW = 0;
    isInterrupted = 0;
    $readmemh("mcu0mi.hex", m);
    for (i=0; i < 127; i=i+2) begin
       $display("%8x: %8x", i, {m[i], m[i+1]});
    end
  end
  
  always @(posedge clock) begin // 在 clock 時脈的正邊緣時觸發
    IR = {m[`PC], m[`PC+1]};    // 指令擷取階段：IR=m[PC], 2 個 Byte 的記憶體
    pc0= `PC;                   // 儲存舊的 PC 值在 pc0 中。
    `PC = `PC+2;                // 擷取完成，PC 前進到下一個指令位址
    case (`OP)                  // 解碼、根據 OP 執行動作
      LD: `A = `M; 		// LD C
      ST: `M = `A;		// ST C
      CMP: begin `N=(`A < `M); `Z=(`A==`M); end // CMP C
      ADD: `A = `A + `M;	// ADD C
      JMP: `PC = `C;		// JMP C
      JEQ: if (`Z) `PC=`C;	// JEQ C
      SWI: $display("SWI C=%d A=%d", `C, `A);
      IRET: begin `PC = `LR; isInterrupted = 0; end      
      LDS: `SP = `M; 		// LS C
      PUSH: begin `SP=`SP-2; {m[`SP], m[`SP+1]} = R[`C3]; end // PUSH C
      POP:  begin R[`C3] = {m[`SP], m[`SP+1]}; `SP=`SP+2; end // POP  C
      default: $display("op=%d , not defined!", `OP);
    endcase
    // 印出 PC, IR, SW, A 等暫存器值以供觀察
    $display("%4dns PC=%x IR=%x, SW=%x, A=%d SP=%x LR=%x", $stime, pc0, IR, `SW, `A, `SP, `LR);
    if (!isInterrupted && interrupt) begin
      isInterrupted = 1;
      `LR = `PC;
      `PC = irq << 1;
    end    
  end
endmodule

module main;                // 測試程式開始
reg clock;                  // 時脈 clock 變數
reg interrupt;
reg [2:0] irq;

cpu cpux(clock, interrupt, irq);            // 宣告 cpu0mc 處理器

initial begin
  clock = 0;          // 一開始 clock 設定為 0
  interrupt = 0;
  irq = 2;
end
always #10 clock=~clock;    // 每隔 10ns 反相，時脈週期為 20ns

always #500 begin 
  interrupt=1;
  #30;
  interrupt=0;
end

initial #4000 $finish;      // 在 3000 奈秒的時候停止測試。

endmodule
