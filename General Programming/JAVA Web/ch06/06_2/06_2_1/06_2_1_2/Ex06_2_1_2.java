import java.net.*;
import java.io.*;

public class Ex06_2_1_2 {
  ServerSocket  SS; 

  public Ex06_2_1_2() {
     try{
         SS = new ServerSocket(1234);
         System.out.println("Server created.");
      }
      catch(IOException e){
           System.out.println(e.getMessage());
      }		
  }

  public static void main(String args[]){
         Ex06_2_1_2 ServerStart=new Ex06_2_1_2();
  }
}
