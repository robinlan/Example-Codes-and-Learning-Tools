import java.net.*;
import java.io.*;

public class Ex06_2_1_1 {
  ServerSocket  SS; 

  public Ex06_2_1_1() {
         SS = new ServerSocket(1234);
         System.out.println("Server created.");
  }

  public static void main(String args[]){
         Ex06_2_1_1 ServerStart=new Ex06_2_1_1();
  }
}
