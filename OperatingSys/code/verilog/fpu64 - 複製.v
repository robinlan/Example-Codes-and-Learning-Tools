// https://instruct1.cit.cornell.edu/courses/ece576/StudentWork/ss868/fp/Reg27FP/FpMul.v
// https://instruct1.cit.cornell.edu/courses/ece576/StudentWork/ss868/fp/Reg27FP/FpAdd.v
// https://instruct1.cit.cornell.edu/courses/ece576/FloatingPoint/index.html#Schneider_fp

`define F_SIGN               63
`define F_EXP                62:52
`define F_FRAC               51:0

// a = (-1)^a.s (1+a.f) * 2 ^ {a.e-1023} 
// b = (-1)^b.s (1+b.f) * 2 ^ {b.e-1023} 
// a*b = (-1)^(a.s xor b.s) (1+a.f) (1+b.f) * 2^{ (a.e+b.e-1023) - 1023}
//       z.s = a.s xor b.s  z.f = tail(...)   z.e = a.e+b.e-1023

// A combinational floating point multiplier.
module fmul(input [63:0] iA, input [63:0] iB, output [63:0] oProd);
    // Extract fields of A and B.
    wire        A_s;
    wire [10:0]  A_e;
    wire [51:0] A_f;
    wire        B_s;
    wire [10:0]  B_e;
    wire [51:0] B_f;
    assign A_s = iA[`F_SIGN];
    assign A_e = iA[`F_EXP];
    assign A_f = iA[`F_FRAC];
    assign B_s = iB[`F_SIGN];
    assign B_e = iB[`F_EXP];
    assign B_f = iB[`F_FRAC];

	
    // XOR sign bits to determine product sign.
    wire   oProd_s;
    assign oProd_s = A_s ^ B_s; // 正負號 p.s = a.s xor b.s

    // Multiply the fractions of A and B 
    wire [105:0] pre_prod_frac;
    assign pre_prod_frac = {1'b1, A_f} * {1'b1, B_f};
	// 小數部分： p.f = {1, a.f} * {1, b.f}

    // Add exponents of A and B
    wire [11:0]  pre_prod_exp;
    assign pre_prod_exp = A_e + B_e - 12'd1023; // 指數部份： p.e = a.e + b.e - 1023

    // If top bit of product frac is 0, shift left one
    wire [10:0]  oProd_e;
    wire [51:0] oProd_f;
//    assign oProd_e = pre_prod_frac[104] ? pre_prod_exp : (pre_prod_exp - 11'b1);
    assign oProd_e = pre_prod_frac[105] ? pre_prod_exp[10:0]+1: pre_prod_exp[10:0];
	
	// 若 p.f[105] == 1，則取 p.f[104:53]，否則取 p.f[103:52]
    assign oProd_f = pre_prod_frac[105] ? pre_prod_frac[104:53] : pre_prod_frac[103:52];

    // Detect underflow
    wire        underflow;
    assign underflow = A_e[10] & B_e[10] & ~oProd_e[10];

    // Detect zero conditions (either product frac doesn't start with 1, or underflow)
    assign oProd = ~oProd_f[51] ? 64'b0 :
                   underflow    ? 64'b0 :
                   {oProd_s, oProd_e, oProd_f};

endmodule

module main;
real x, y, z;
reg [63:0] x1, y1;
wire [63:0] z1;

fmul f1(x1, y1, z1);

initial 
begin
  x=7.00;
  y=-9.00;
  x1 = $realtobits(x);
  y1 = $realtobits(y);
  #100;
  $display("A_s=%b A_e=%b A_f=%b", f1.A_s, f1.A_e, f1.A_f);
  $display("B_s=%b B_e=%b B_f=%b", f1.B_s, f1.B_e, f1.B_f);
  $display("pre_prod_exp=%b \npre_prod_frac=%b \nunderflow=%b", f1.pre_prod_exp, f1.pre_prod_frac, f1.underflow);
  $display("oProd_s=%b oProd_e=%b oProd_f=%b", f1.oProd_s, f1.oProd_e, f1.oProd_f);

  z = $bitstoreal(z1);
  $display("x=%f y=%f z=%f", x, y, z);
  $display("x1=%b \ny1=%b \nz1=%b", x1, y1, z1);
end

endmodule



/*
module FpAdd (input  iCLK, input [31:0] iA, input [31:0] iB, output [31:0] oSum);
    // Extract fields of A and B.
    wire        A_s;
    wire [7:0]  A_e;
    wire [22:0] A_f;
    wire        B_s;
    wire [7:0]  B_e;
    wire [22:0] B_f;
    assign A_s = iA[`F_SIGN];
    assign A_e = iA[`F_EXP];
    assign A_f = iA[`F_FRAC];
    assign B_s = iB[`F_SIGN];
    assign B_e = iB[`F_EXP];
    assign B_f = iB[`F_FRAC];

    // Shift fractions of A and B so that they align.
    wire [8:0]  exp_diff_A;
    wire [8:0]  exp_diff_B;
    wire [7:0]  larger_exp;
    wire [46:0] A_f_shifted;
    wire [46:0] B_f_shifted;

    assign exp_diff_A = {B_e[7], B_e} - {A_e[7], A_e};
    assign exp_diff_B = {A_e[7], A_e} - {B_e[7], B_e};
    assign larger_exp = exp_diff_B[8] ? B_e : A_e;
    assign A_f_shifted = exp_diff_A[8]        ? {1'b0,  A_f, 23'b0} :
                         (exp_diff_A > 9'd35) ? 37'b0 :
                         ({1'b0, A_f, 23'b0} >> exp_diff_A);
    assign B_f_shifted = exp_diff_B[8]        ? {1'b0,  B_f, 23'b0} :
                         (exp_diff_B > 9'd35) ? 37'b0 :
                         ({1'b0, B_f, 23'b0} >> exp_diff_B);

    // Determine which of A, B is larger
    wire A_larger;
    assign A_larger = exp_diff_A[8] | (~exp_diff_B[8] & (A_f > B_f));

    // Calculate sum or difference of shifted fractions.
    wire [46:0] pre_sum;
    assign pre_sum = ((A_s^B_s) &  A_larger) ? A_f_shifted - B_f_shifted :
                     ((A_s^B_s) & ~A_larger) ? B_f_shifted - A_f_shifted :
                     A_f_shifted + B_f_shifted;

    // buffer midway results
    reg  [46:0] buf_pre_sum;
    reg  [7:0]  buf_larger_exp;
    reg         buf_A_f_zero;
    reg         buf_B_f_zero;
    reg  [31:0] buf_A;
    reg  [31:0] buf_B;
    reg         buf_oSum_s;
    always @(posedge iCLK) begin
        buf_pre_sum    <= pre_sum;
        buf_larger_exp <= larger_exp;
        buf_A_f_zero   <= (A_f == 23'b0);
        buf_B_f_zero   <= (B_f == 23'b0);
        buf_A          <= iA;
        buf_B          <= iB;
        buf_oSum_s     <= A_larger ? A_s : B_s;
    end

    // Convert to positive fraction and a sign bit.
    wire [36:0] pre_frac;
    assign pre_frac = buf_pre_sum;

    // Determine output fraction and exponent change with position of first 1.
    wire [17:0] oSum_f;
    wire [7:0]  shft_amt;
    assign shft_amt = pre_frac[46] ? 8'd0  : pre_frac[45] ? 8'd1  :
                      pre_frac[44] ? 8'd2  : pre_frac[43] ? 8'd3  :
                      pre_frac[42] ? 8'd4  : pre_frac[41] ? 8'd5  :
                      pre_frac[40] ? 8'd6  : pre_frac[39] ? 8'd7  :
                      pre_frac[38] ? 8'd8  : pre_frac[37] ? 8'd9  :
                      pre_frac[36] ? 8'd10 : pre_frac[35] ? 8'd11 :
                      pre_frac[34] ? 8'd12 : pre_frac[33] ? 8'd13 :
                      pre_frac[32] ? 8'd14 : pre_frac[31] ? 8'd15 :
                      pre_frac[30] ? 8'd16 : pre_frac[29] ? 8'd17 :
                      pre_frac[28] ? 8'd18 : pre_frac[27] ? 8'd19 :
                      pre_frac[26] ? 8'd20 : pre_frac[25] ? 8'd21 :
                      pre_frac[24] ? 8'd22 : pre_frac[23] ? 8'd23 :
                      pre_frac[22] ? 8'd24 : pre_frac[21] ? 8'd25 :
                      pre_frac[20] ? 8'd26 : pre_frac[19] ? 8'd27 :
                      pre_frac[18] ? 8'd28 : pre_frac[17] ? 8'd29 :
                      pre_frac[16] ? 8'd30 : pre_frac[15] ? 8'd31 :
                      pre_frac[14] ? 8'd32 : pre_frac[13] ? 8'd33 :
                      pre_frac[12] ? 8'd34 : pre_frac[11] ? 8'd35 :
                      pre_frac[10] ? 8'd36 : pre_frac[9]  ? 8'd37 :
                      pre_frac[8]  ? 8'd38 : pre_frac[7]  ? 8'd39 :
                      pre_frac[6]  ? 8'd40 : pre_frac[5]  ? 8'd41 :
                      pre_frac[4]  ? 8'd42 : pre_frac[3]  ? 8'd43 :
                      pre_frac[3]  ? 8'd44 : pre_frac[1]  ? 8'd45 : 
					  8'd46;

    wire [63:0] pre_frac_shft;
    assign pre_frac_shft = {pre_frac, 22'b0} << shft_amt;
    assign oSum_f = pre_frac_shft[63:46];

    wire [7:0] oSum_e;
    assign oSum_e = buf_larger_exp - shft_amt + 8'b1;

    // Detect underflow
    wire underflow;
    assign underflow = ~oSum_e[7] && buf_larger_exp[7] && (shft_amt != 8'b0);

    assign oSum = buf_A_f_zero ? buf_B :
                  buf_B_f_zero ? buf_A :
                  (pre_frac == 38'b0) ? 31'b0 :
                  underflow ? 31'b0 :
                  {buf_oSum_s, oSum_e, oSum_f};

endmodule
*/
