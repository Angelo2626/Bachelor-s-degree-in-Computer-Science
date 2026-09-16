`timescale 1ns / 1ps

module tb_FSMD();

  // File descriptors.
  integer tbf, outf;
  
  reg clk, i;
  reg[1:0] p;
  reg[1:0] s;
  reg pr1, pr0, se1, se0, m1, m0;
  wire[1:0] m;
  wire[2:0] state;
  wire pa1, pa0;
  
  FSMD fsmd(clk, i, p, s, pa1, pa0, m, state);
 
  always #10 clk = ~clk;
  
  always #1 begin
    pr1=p/2'b10;
    pr0=p%2'b10;
    se1=s/2'b10;
    se0=s%2'b10;
    m1 = m/2'b10;
    m0 = m%2'b10;
  end
  
  initial begin
    $dumpfile("dump.vcd");
    $dumpvars(1);
    tbf = $fopen("testbench.script", "w");
    outf = $fopen("output_verilog.txt", "w");
    $fdisplay(tbf, "read_blif FSMD.blif");
    
    clk = 1'b1;
	i = 1;
    p = 2'b00;
    s = 2'b10;
	#11
    $fdisplay(tbf, "simulate %b %b %b %b %b", pr1, pr0, se1, se0, i);
    #10
    $fdisplay(outf, "Outputs: %b %b %b %b", m1, m0, pa1, pa0);
    
    i = 0;
    p = 2'b01;
    s = 2'b11;
	#10
    $fdisplay(tbf, "simulate %b %b %b %b %b", pr1, pr0, se1, se0, i);
    #10
    $fdisplay(outf, "Outputs: %b %b %b %b", m1, m0, pa1, pa0);
    
    i = 0;
    p = 2'b10;
    s = 2'b01;
	#10
    $fdisplay(tbf, "simulate %b %b %b %b %b", pr1, pr0, se1, se0, i);
    #10
    $fdisplay(outf, "Outputs: %b %b %b %b", m1, m0, pa1, pa0);
    

    
    i = 0;
    p = 2'b10;
    s = 2'b01;
	#10
    $fdisplay(tbf, "simulate %b %b %b %b %b", pr1, pr0, se1, se0, i);
    #10
    $fdisplay(outf, "Outputs: %b %b %b %b", m1, m0, pa1, pa0);
    
    i = 0;
    p = 2'b11;
    s = 2'b11;
	#10
    $fdisplay(tbf, "simulate %b %b %b %b %b", pr1, pr0, se1, se0, i);
    #10
    $fdisplay(outf, "Outputs: %b %b %b %b", m1, m0, pa1, pa0);
    
    i = 0;
    p = 2'b01;
    s = 2'b11;
	#10
    $fdisplay(tbf, "simulate %b %b %b %b %b", pr1, pr0, se1, se0, i);
    #10
    $fdisplay(outf, "Outputs: %b %b %b %b", m1, m0, pa1, pa0);
    
    i = 0;
    p = 2'b01;
    s = 2'b10;
	#10
    $fdisplay(tbf, "simulate %b %b %b %b %b", pr1, pr0, se1, se0, i);
    #10
    $fdisplay(outf, "Outputs: %b %b %b %b", m1, m0, pa1, pa0);
    
    i = 0;
    p = 2'b11;
    s = 2'b10;
	#10
    $fdisplay(tbf, "simulate %b %b %b %b %b", pr1, pr0, se1, se0, i);
    #10
    $fdisplay(outf, "Outputs: %b %b %b %b", m1, m0, pa1, pa0);

    $fdisplay(tbf, "quit");
    $fclose(tbf);    
    $fclose(outf);   
    $finish;
   
  end
endmodule
