import java.io.*;
import java.net.*;

public class Client11_6 {
  static String iaddr;
  static int port;
  
  public Client11_6() {
     try{
         Socket socket=new Socket(InetAddress.getByName(iaddr),port);
         System.out.println("To connect Server");
         socket.close();
      }
      catch(IOException e){
         System.out.println(e.getMessage());
      } 
  }

  public static void main(String args[]) {
      if (args.length < 2){
         System.out.println("USAGE: java Client11_6 [iaddr] [port]");
         System.exit(1);
      }
      iaddr = args[0];
      port = Integer.parseInt(args[1]);
      Client11_6 ClientStart=new Client11_6();
  }
}
