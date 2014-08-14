import java.io.*;
import java.net.*;

public class Client12_4_1 {
  static String iaddr;
  static int port;
  static String messageout;
  
  public Client12_4_1() {
     try{
         Socket socket=new Socket(InetAddress.getByName(iaddr),port);

         DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
         outstream.writeUTF(messageout);
	 System.out.println("Message is transferred to Server");

         socket.close(); 
      }

      catch(IOException e){
         System.out.println(e.getMessage());
      }
  }

  public static void main(String args[]) {
      if (args.length < 3){
         System.out.println("USAGE: java Client12_4_1 [iaddr] [port] [messageout]");
         System.exit(1);
      }

      iaddr = args[0];
      port=Integer.parseInt(args[1]);
      messageout = args[2];
      Client12_4_1 ClientStart=new Client12_4_1();
  }
}
