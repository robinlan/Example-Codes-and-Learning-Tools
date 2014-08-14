import java.io.*;
import java.net.*;

public class Client11_4_2 { 
  public Client11_4_2() {
     try{
         InetAddress iaddr = InetAddress.getByName("163.15.40.242");
         Socket socket = new Socket(iaddr, 1234);
         System.out.println("Client Local Port = " + socket.getLocalPort());
         System.out.println("Server Port = " + socket.getPort());
         socket.close();
      }
      catch(IOException e){
         System.out.println(e.getMessage()); 
      }
  }

  public static void main(String args[]) {
      Client11_4_2 ClientStart=new Client11_4_2();
  }
}
