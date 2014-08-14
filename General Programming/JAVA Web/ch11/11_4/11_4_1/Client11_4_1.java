import java.io.*;
import java.net.*;

public class Client11_4_1 { 
  public Client11_4_1() {
     try{
         Socket socket = new Socket("163.15.40.242", 1234);
         socket.close();
      }
      catch(IOException e){
         System.out.println(e.getMessage()); 
      }
  }

  public static void main(String args[]) {
      Client11_4_1 ClientStart=new Client11_4_1();
  }
}
