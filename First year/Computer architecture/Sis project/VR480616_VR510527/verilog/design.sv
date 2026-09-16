module FSMD(input clk, i, input[1:0] p, input[1:0] s, output reg pa1, pa0, output reg[1:0] m, output reg[2:0] state);
  
  reg[2:0] stato = 3'b000;
  reg[2:0] statoprossimo = 3'b000;
  reg[2:0] p1 = 2'b00;
  reg[2:0] s1 = 2'b00;
  reg mi = 0; //MIN
  reg ma = 0; //MAX
  reg[3:0] contamanche = 4'b0000;
  reg[3:0] maxmanche;
  
  always @(negedge clk) begin //UPDATE STATE
    stato = statoprossimo;
    state = stato;
  end
    
  always @(posedge clk) begin //MIN + MAX  
    if(contamanche > 4'b0011) begin
      mi = 1'b1;
    end if(contamanche == maxmanche) begin
      ma = 1'b1;
    end
  end
  
  always @(negedge clk) begin //MANCHE + LAST MOVE + MAXMANCHE
    if(i) begin
      m=2'b00;
      statoprossimo = 3'b000;
      maxmanche = p*4'b0100 + s + 4'b0100; 
      pa1 = 1'b0;
      pa0 = 1'b0;
    end else begin
      if(p==p1) begin
        m = 2'b00;
      end else if(s==s1) begin
        m = 2'b00;
      end else if(p==s) begin
        m = 2'b11;
        contamanche += 1;
      end else if(p==2'b01 && s==2'b11) begin
        m = 2'b01;
        contamanche += 1;
      end else if (p==2'b01 && s==2'b10) begin
        m = 2'b10;
        contamanche += 1;
      end else if(p==2'b10 && s==2'b01) begin
        m = 2'b01;
        contamanche += 1;
      end else if(p==2'b10 && s==2'b11) begin
        m = 2'b10;
        contamanche += 1;
      end else if(p==2'b11 && s==2'b01) begin
        m = 2'b10;
        contamanche += 1;
      end else if(p==2'b11 && s==2'b10) begin
        m = 2'b01;
        contamanche += 1;
      end else if(p==2'b00 || s==2'b00) begin
        m = 2'b00;
      end
    end
  end


  
  always @(posedge clk) begin //FSM
      case(stato)
        3'b000: //IP
          case(m)
            2'b11: //PAREGGIO
              if(ma) begin
                statoprossimo = 3'b000;
                pa1 = 1;
                pa0 = 1;
                p1 = 2'b00;
                s1 = 2'b00;
              end else begin
                p1 = 2'b00;
                s1 = 2'b00;
              end
            2'b01: //VITTORIA PRIMO
              if(ma) begin
                statoprossimo = 3'b011;
                pa1 = 0;
                pa0 = 1;
                s1 = 2'b00;
                p1 = p;
              end else begin
                statoprossimo = 3'b001;
                pa1 = 0;
                pa0 = 0;
                s1 = 2'b00;
                p1 = p;
              end
            2'b10: //VITTORIA SECONDO
              if(ma) begin
                statoprossimo = 3'b110;
                pa1 = 1;
                pa0 = 0;
                p1 = 2'b00;
                s1 = s;
              end else begin
                statoprossimo = 3'b100;
                pa1 = 0;
                pa0 = 0;
                p1 = 2'b00;
                s1 = s;
              end
            2'b00: //MANCHE INVALIDA
              begin
                statoprossimo = 3'b000;
              end
          endcase
        3'b001: //VP1
          case(m)
            2'b11: //PAREGGIO
              if(ma) begin
                statoprossimo = 3'b011;
                pa1 = 0;
                pa0 = 1;
                p1 = 2'b00;
                s1 = 2'b00;
              end else begin
                p1 = 2'b00;
                s1 = 2'b00;
              end
            2'b01: //VITTORIA PRIMO
              if(mi) begin
                statoprossimo = 3'b010;
                pa1 = 0;
                pa0 = 1;
                s1 = 2'b00;
                p1 = p;
              end else begin
                statoprossimo = 3'b010;
                pa1 = 0;
                pa0 = 0;
                s1 = 2'b00;
                p1 = p;
              end
            2'b10: //VITTORIA SECONDO
              if(ma) begin
                statoprossimo = 3'b000;
                pa1 = 1;
                pa0 = 1;
                p1 = 2'b00;
                s1 = s;
              end else begin
                statoprossimo = 3'b000;
                pa1 = 0;
                pa0 = 0;
                p1 = 2'b00;
                s1 = s;
              end
            2'b00: //MANCHE INVALIDA
              begin
                statoprossimo = 3'b001;
              end
          endcase
        3'b010: //VP2
          case(m)
            2'b11: //PAREGGIO
              if(mi) begin
                statoprossimo = 3'b011;
                pa1 = 0;
                pa0 = 1;
                p1 = 2'b00;
                s1 = 2'b00;
              end else begin
                p1 = 2'b00;
                s1 = 2'b00;
              end
            2'b01: //VITTORIA PRIMO
              if(mi) begin
                statoprossimo = 3'b011;
                pa1 = 0;
                pa0 = 1;
                s1 = 2'b00;
                p1 = p;
              end else begin
                statoprossimo = 3'b011;
                pa1 = 0;
                pa0 = 0;
                s1 = 2'b00;
                p1 = p;
              end
            2'b10: //VITTORIA SECONDO
              if(ma) begin
                statoprossimo = 3'b011;
                pa1 = 0;
                pa0 = 1;
                p1 = 2'b00;
                s1 = s;
              end else begin
                statoprossimo = 3'b001;
                pa1 = 0;
                pa0 = 0;
                p1 = 2'b00;
                s1 = s;
              end
            2'b00: //MANCHE INVALIDA
              begin
                statoprossimo = 3'b010;
              end
          endcase
        3'b011: //FP
          case(m)
            2'b11: //PAREGGIO
              begin
          		statoprossimo = 3'b011;
          		pa1 = 0;
          		pa0 = 1;
                s1 = 2'b00;
                p1 = 2'b00;
              end 
            2'b01: //VITTORIA PRIMO
              begin
          		statoprossimo = 3'b011;
          		pa1 = 0;
          		pa0 = 1;
                s1 = 2'b00;
                p1 = p;
              end
            2'b10: //VITTORIA SECONDO
              begin
          		statoprossimo = 3'b011;
          		pa1 = 0;
          		pa0 = 1;
                p1 = 2'b00;
                s1 = s;
          	  end
            2'b00: //MANCHE INVALIDA
              begin
                statoprossimo = 3'b011;
              end
          endcase
          
        3'b100: //VS1
          case(m)
            2'b11: //PAREGGIO
              if(ma) begin
                statoprossimo = 3'b110;
                pa1 = 1;
                pa0 = 0;
                p1 = 2'b00;
                s1 = 2'b00;
              end else begin
                p1 = 2'b00;
                s1 = 2'b00;
              end
            2'b10: //VITTORIA SECONDO
              if(mi) begin
                statoprossimo = 3'b110;
                pa1 = 1;
                pa0 = 0;
                p1 = 2'b00;
                s1 = s;
              end else begin
                statoprossimo = 3'b101;
                pa1 = 0;
                pa0 = 0;
                p1 = 2'b00;
                s1 = s;
              end
            2'b01: //VITTORIA PRIMO
              if(ma) begin
                statoprossimo = 3'b000;
                pa1 = 1;
                pa0 = 1;
                s1 = 2'b00;
                p1 = p;
              end else begin
                statoprossimo = 3'b000;
                pa1 = 0;
                pa0 = 0;
                s1 = 2'b00;
                p1 = p;
              end
            2'b00: //MANCHE INVALIDA
              begin
                statoprossimo = 3'b100;
              end
          endcase
        3'b101: //VS2
          case(m)
            2'b11: //PAREGGIO
              if(mi) begin
                statoprossimo = 3'b110;
                pa1 = 1;
                pa0 = 0;
                p1 = 2'b00;
                s1 = 2'b00;
              end else begin
                p1 = 2'b00;
                s1 = 2'b00;
              end
            2'b10: //VITTORIA SECONDO
              if(mi) begin
                statoprossimo = 3'b110;
                pa1 = 1;
                pa0 = 0;
                p1 = 2'b00;
                s1 = s;
              end else begin
                statoprossimo = 3'b110;
                pa1 = 0;
                pa0 = 0;
                p1 = 2'b00;
                s1 = s;
              end
            2'b01: //VITTORIA PRIMO
              if(ma) begin
                statoprossimo = 3'b110;
                pa1 = 1;
                pa0 = 0;
                s1 = 2'b00;
                p1 = p;
              end else begin
                statoprossimo = 3'b100;
                pa1 = 0;
                pa0 = 0;
                s1 = 2'b00;
                p1 = p;
              end
            2'b00: //MANCHE INVALIDA
              begin
                statoprossimo = 3'b101;
              end
          endcase
        3'b110: //FS
          case(m)
            2'b11: //PAREGGIO
              begin
          		statoprossimo = 3'b110;
          		pa1 = 1;
          		pa0 = 0;
                s1 = 2'b00;
                p1 = 2'b00;
              end
            2'b01: //VITTORIA PRIMO
              begin
          		statoprossimo = 3'b110;
          		pa1 = 1;
          		pa0 = 0;
                s1 = 2'b00;
                p1 = p;
              end
            2'b10: //VITTORIA SECONDO
              begin
          		statoprossimo = 3'b110;
          		pa1 = 1;
          		pa0 = 0;
                p1 = 2'b00;
                s1 = s;
          	  end
            2'b00: //MANCHE INVALIDA
              begin
                statoprossimo = 3'b110;
              end
          endcase
        endcase
  end       
endmodule