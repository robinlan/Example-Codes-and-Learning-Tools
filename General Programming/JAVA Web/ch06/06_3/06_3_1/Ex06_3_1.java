import java.net.*;
import java.io.*;

public class Ex06_3_1 {
  ServerSocket  SS; 

  public Ex06_3_1() throws IOException {
         SS = new ServerSocket(1234);
         System.out.println("Server created.");
  }

  public static void main(String args[]) throws IOException{
         Ex06_3_1 ServerStart=new Ex06_3_1();
  }
}
