module master(input clock, output reg [7:0] address, inout [7:0] data);
initial begin
  address = 0;
  #100 address=8'hF0;
end
  always @(data) begin
    $display("master:%4dns data=%x", $stime, data);
  end  
endmodule

module device(input clock, input [7:0] address, output reg [7:0] data);
  always @(clock or address) begin
    if (address == 8'hF0)
	  data = #1 8'he3;
	else
	  data = #1 8'hZ;
  end
endmodule

module main;
reg clock;
wire [7:0] abus, dbus;

master m(clock, abus, dbus);
device d(clock, abus, dbus);

initial begin
  $monitor("%4dns address=%x data=%x", $stime, abus, dbus);
  #300 $finish;
end

always #5 begin 
  clock=~clock;    // 每隔 5ns 反相，時脈週期為 10ns
end  
endmodule
