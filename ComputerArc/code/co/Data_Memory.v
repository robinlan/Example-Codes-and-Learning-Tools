`timescale 1ns / 1ps
//////////////////////////////////////////////////////////////////////////////////
// Company: 
// Engineer: 
// 
// Create Date:    17:31:32 08/18/2010 
// Design Name: 
// Module Name:    Data_Memory 
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
module Data_Memory
(
	clk_i,
	addr_i,
	data_i,
	MemRead_i,
	MemWrite_i,
	data_o
);

// Interface
input				clk_i;
input	[31:0]		addr_i;
input	[31:0]		data_i;
input				MemRead_i;
input				MemWrite_i;
output	[31:0] 		data_o;

// Signals
reg		[31:0]		data_o;

// Memory
reg		[7:0]		Mem 			[0:511];	// address: 0x00~0x80
integer				i;

// For Testbench to debug
wire	[31:0]		memory			[0:127];
assign  memory[0] = {Mem[3], Mem[2], Mem[1], Mem[0]};
assign  memory[1] = {Mem[7], Mem[6], Mem[5], Mem[4]};
assign  memory[2] = {Mem[11], Mem[10], Mem[9], Mem[8]};
assign  memory[3] = {Mem[15], Mem[14], Mem[13], Mem[12]};
assign  memory[4] = {Mem[19], Mem[18], Mem[17], Mem[16]};
assign  memory[5] = {Mem[23], Mem[22], Mem[21], Mem[20]};
assign  memory[6] = {Mem[27], Mem[26], Mem[25], Mem[24]};
assign  memory[7] = {Mem[31], Mem[30], Mem[29], Mem[28]};
assign  memory[8] = {Mem[35], Mem[34], Mem[33], Mem[32]};
assign  memory[9] = {Mem[39], Mem[38], Mem[37], Mem[36]};
assign  memory[10] = {Mem[43], Mem[42], Mem[41], Mem[40]};
assign  memory[11] = {Mem[47], Mem[46], Mem[45], Mem[44]};
assign  memory[12] = {Mem[51], Mem[50], Mem[49], Mem[48]};
assign  memory[13] = {Mem[55], Mem[54], Mem[53], Mem[52]};
assign  memory[14] = {Mem[59], Mem[58], Mem[57], Mem[56]};
assign  memory[15] = {Mem[63], Mem[62], Mem[61], Mem[60]};
assign  memory[16] = {Mem[67], Mem[66], Mem[65], Mem[64]};
assign  memory[17] = {Mem[71], Mem[70], Mem[69], Mem[68]};
assign  memory[18] = {Mem[75], Mem[74], Mem[73], Mem[72]};
assign  memory[19] = {Mem[79], Mem[78], Mem[77], Mem[76]};
assign  memory[20] = {Mem[83], Mem[82], Mem[81], Mem[80]};
assign  memory[21] = {Mem[87], Mem[86], Mem[85], Mem[84]};
assign  memory[22] = {Mem[91], Mem[90], Mem[89], Mem[88]};
assign  memory[23] = {Mem[95], Mem[94], Mem[93], Mem[92]};
assign  memory[24] = {Mem[99], Mem[98], Mem[97], Mem[96]};
assign  memory[25] = {Mem[103], Mem[102], Mem[101], Mem[100]};
assign  memory[26] = {Mem[107], Mem[106], Mem[105], Mem[104]};
assign  memory[27] = {Mem[111], Mem[110], Mem[109], Mem[108]};
assign  memory[28] = {Mem[115], Mem[114], Mem[113], Mem[112]};
assign  memory[29] = {Mem[119], Mem[118], Mem[117], Mem[116]};
assign  memory[30] = {Mem[123], Mem[122], Mem[121], Mem[120]};
assign  memory[31] = {Mem[127], Mem[126], Mem[125], Mem[124]};

assign  memory[32] = {Mem[131], Mem[130], Mem[129], Mem[128]};
assign  memory[33] = {Mem[135], Mem[134], Mem[133], Mem[132]};
assign  memory[34] = {Mem[139], Mem[138], Mem[137], Mem[136]};
assign  memory[35] = {Mem[143], Mem[142], Mem[141], Mem[140]};
assign  memory[36] = {Mem[147], Mem[146], Mem[145], Mem[144]};
assign  memory[37] = {Mem[151], Mem[150], Mem[149], Mem[148]};
assign  memory[38] = {Mem[155], Mem[154], Mem[153], Mem[152]};
assign  memory[39] = {Mem[159], Mem[158], Mem[157], Mem[156]};
assign  memory[40] = {Mem[163], Mem[162], Mem[161], Mem[160]};
assign  memory[41] = {Mem[167], Mem[166], Mem[165], Mem[164]};
assign  memory[42] = {Mem[171], Mem[170], Mem[169], Mem[168]};
assign  memory[43] = {Mem[175], Mem[174], Mem[173], Mem[172]};
assign  memory[44] = {Mem[179], Mem[178], Mem[177], Mem[176]};
assign  memory[45] = {Mem[183], Mem[182], Mem[181], Mem[180]};
assign  memory[46] = {Mem[187], Mem[186], Mem[185], Mem[184]};
assign  memory[47] = {Mem[191], Mem[190], Mem[189], Mem[188]};
assign  memory[48] = {Mem[195], Mem[194], Mem[193], Mem[192]};
assign  memory[49] = {Mem[199], Mem[198], Mem[197], Mem[196]};
assign  memory[50] = {Mem[203], Mem[202], Mem[201], Mem[200]};
assign  memory[51] = {Mem[207], Mem[206], Mem[205], Mem[204]};
assign  memory[52] = {Mem[211], Mem[210], Mem[209], Mem[208]};
assign  memory[53] = {Mem[215], Mem[214], Mem[213], Mem[212]};
assign  memory[54] = {Mem[219], Mem[218], Mem[217], Mem[216]};
assign  memory[55] = {Mem[223], Mem[222], Mem[221], Mem[220]};
assign  memory[56] = {Mem[227], Mem[226], Mem[225], Mem[224]};
assign  memory[57] = {Mem[231], Mem[230], Mem[229], Mem[228]};
assign  memory[58] = {Mem[235], Mem[234], Mem[233], Mem[232]};
assign  memory[59] = {Mem[239], Mem[238], Mem[237], Mem[236]};
assign  memory[60] = {Mem[243], Mem[242], Mem[241], Mem[240]};
assign  memory[61] = {Mem[247], Mem[246], Mem[245], Mem[244]};
assign  memory[62] = {Mem[251], Mem[250], Mem[249], Mem[248]};
assign  memory[63] = {Mem[255], Mem[254], Mem[253], Mem[252]};
assign  memory[64] = {Mem[259], Mem[258], Mem[257], Mem[256]};
assign  memory[65] = {Mem[263], Mem[262], Mem[261], Mem[260]};
assign  memory[66] = {Mem[267], Mem[266], Mem[265], Mem[264]};
assign  memory[67] = {Mem[271], Mem[270], Mem[269], Mem[268]};
assign  memory[68] = {Mem[275], Mem[274], Mem[273], Mem[272]};
assign  memory[69] = {Mem[279], Mem[278], Mem[277], Mem[276]};
assign  memory[70] = {Mem[283], Mem[282], Mem[281], Mem[280]};
assign  memory[71] = {Mem[287], Mem[286], Mem[285], Mem[284]};
assign  memory[72] = {Mem[291], Mem[290], Mem[289], Mem[288]};
assign  memory[73] = {Mem[295], Mem[294], Mem[293], Mem[292]};
assign  memory[74] = {Mem[299], Mem[298], Mem[297], Mem[296]};
assign  memory[75] = {Mem[303], Mem[302], Mem[301], Mem[300]};
assign  memory[76] = {Mem[307], Mem[306], Mem[305], Mem[304]};
assign  memory[77] = {Mem[311], Mem[310], Mem[309], Mem[308]};
assign  memory[78] = {Mem[315], Mem[314], Mem[313], Mem[312]};
assign  memory[79] = {Mem[319], Mem[318], Mem[317], Mem[316]};
assign  memory[80] = {Mem[323], Mem[322], Mem[321], Mem[320]};
assign  memory[81] = {Mem[327], Mem[326], Mem[325], Mem[324]};
assign  memory[82] = {Mem[331], Mem[330], Mem[329], Mem[328]};
assign  memory[83] = {Mem[335], Mem[334], Mem[333], Mem[332]};
assign  memory[84] = {Mem[339], Mem[338], Mem[337], Mem[336]};
assign  memory[85] = {Mem[343], Mem[342], Mem[341], Mem[340]};
assign  memory[86] = {Mem[347], Mem[346], Mem[345], Mem[344]};
assign  memory[87] = {Mem[351], Mem[350], Mem[349], Mem[348]};
assign  memory[88] = {Mem[355], Mem[354], Mem[353], Mem[352]};
assign  memory[89] = {Mem[359], Mem[358], Mem[357], Mem[356]};
assign  memory[90] = {Mem[363], Mem[362], Mem[361], Mem[360]};
assign  memory[91] = {Mem[367], Mem[366], Mem[365], Mem[364]};
assign  memory[92] = {Mem[371], Mem[370], Mem[369], Mem[368]};
assign  memory[93] = {Mem[375], Mem[374], Mem[373], Mem[372]};
assign  memory[94] = {Mem[379], Mem[378], Mem[377], Mem[376]};
assign  memory[95] = {Mem[383], Mem[382], Mem[381], Mem[380]};
assign  memory[96] = {Mem[387], Mem[386], Mem[385], Mem[384]};
assign  memory[97] = {Mem[391], Mem[390], Mem[389], Mem[388]};
assign  memory[98] = {Mem[395], Mem[394], Mem[393], Mem[392]};
assign  memory[99] = {Mem[399], Mem[398], Mem[397], Mem[396]};
assign  memory[100] = {Mem[403], Mem[402], Mem[401], Mem[400]};
assign  memory[101] = {Mem[407], Mem[406], Mem[405], Mem[404]};
assign  memory[102] = {Mem[411], Mem[410], Mem[409], Mem[408]};
assign  memory[103] = {Mem[415], Mem[414], Mem[413], Mem[412]};
assign  memory[104] = {Mem[419], Mem[418], Mem[417], Mem[416]};
assign  memory[105] = {Mem[423], Mem[422], Mem[421], Mem[420]};
assign  memory[106] = {Mem[427], Mem[426], Mem[425], Mem[424]};
assign  memory[107] = {Mem[431], Mem[430], Mem[429], Mem[428]};
assign  memory[108] = {Mem[435], Mem[434], Mem[433], Mem[432]};
assign  memory[109] = {Mem[439], Mem[438], Mem[437], Mem[436]};
assign  memory[110] = {Mem[443], Mem[442], Mem[441], Mem[440]};
assign  memory[111] = {Mem[447], Mem[446], Mem[445], Mem[444]};
assign  memory[112] = {Mem[451], Mem[450], Mem[449], Mem[448]};
assign  memory[113] = {Mem[455], Mem[454], Mem[453], Mem[452]};
assign  memory[114] = {Mem[459], Mem[458], Mem[457], Mem[456]};
assign  memory[115] = {Mem[463], Mem[462], Mem[461], Mem[460]};
assign  memory[116] = {Mem[467], Mem[466], Mem[465], Mem[464]};
assign  memory[117] = {Mem[471], Mem[470], Mem[469], Mem[468]};
assign  memory[118] = {Mem[475], Mem[474], Mem[473], Mem[472]};
assign  memory[119] = {Mem[479], Mem[478], Mem[477], Mem[476]};
assign  memory[120] = {Mem[483], Mem[482], Mem[481], Mem[480]};
assign  memory[121] = {Mem[487], Mem[486], Mem[485], Mem[484]};
assign  memory[122] = {Mem[491], Mem[490], Mem[489], Mem[488]};
assign  memory[123] = {Mem[495], Mem[494], Mem[493], Mem[492]};
assign  memory[124] = {Mem[499], Mem[498], Mem[497], Mem[496]};
assign  memory[125] = {Mem[503], Mem[502], Mem[501], Mem[500]};
assign  memory[126] = {Mem[507], Mem[506], Mem[505], Mem[504]};
assign  memory[127] = {Mem[511], Mem[510], Mem[509], Mem[508]};


initial begin
	for(i=0; i<511; i=i+1)
		Mem[i] = 8'b0;
		/*
	Mem[0] = 8'b0100;
	Mem[4] = 8'b0101;
	Mem[8] = 8'b0110;
	Mem[12] = 8'b0111;
	Mem[16] = 8'b1000;
	Mem[20] = 8'b1001;
	Mem[24] = 8'b1010;
	Mem[28] = 8'b0010;
	Mem[32] = 8'b0001;
	Mem[36] = 8'b0011;*/
end 

always@(posedge clk_i) begin
    if(MemWrite_i) begin
		Mem[addr_i+3] <= data_i[31:24];
		Mem[addr_i+2] <= data_i[23:16];
		Mem[addr_i+1] <= data_i[15:8];
		Mem[addr_i]   <= data_i[7:0];
	end
end

always@(addr_i or MemRead_i) begin
	if(MemRead_i)
		data_o = {Mem[addr_i+3], Mem[addr_i+2], Mem[addr_i+1], Mem[addr_i]};
end

endmodule

