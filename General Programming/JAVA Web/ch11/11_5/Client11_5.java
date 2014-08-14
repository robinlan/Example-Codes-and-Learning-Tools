import java.io.*;
import java.net.*;

public class Client11_5 {
  static String iaddr;
  static int port;
 
  public Client11_5() {
     try{			
         Socket socket=new Socket(InetAddress.getByName(iaddr),port);
         socket.close();
      }
      catch(IOException e){
         System.out.println(e.getMessage());
      }
  }

  public static void main(String args[]) {
      if (args.length < 2){
         System.out.println("USAGE: java Client11_5 [iaddr] [port]");
         System.exit(1);
      }
      iaddr = args[0];
      port =Integer.parseInt(args[1]);
      Client11_5 ClientStart=new Client11_5();
  }
}
