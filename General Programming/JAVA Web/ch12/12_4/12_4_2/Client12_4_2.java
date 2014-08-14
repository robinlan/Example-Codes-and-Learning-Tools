import java.io.*;
import java.net.*;

public class Client12_4_2 {
  static String messageout;
  static String iaddr;
  static int port;
  
  public Client12_4_2() {
     try{
         Socket socket=new Socket(InetAddress.getByName(iaddr), port);

         DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
         outstream.writeUTF(messageout);
	
         DataInputStream instream=new DataInputStream(socket.getInputStream());
         String messagein = instream.readUTF();
         System.out.println("messagein = " + messagein);

         socket.close(); 
      }

      catch(IOException e){
         System.out.println(e.getMessage());
      }
  }

  public static void main(String args[]) {
      if (args.length < 3){
         System.out.println("USAGE: java Client12_4_2 [iaddr] [port] [messageout]");
         System.exit(1);
      }

      iaddr = args[0];
      port=Integer.parseInt(args[1]);
      messageout = args[2];
      Client12_4_2 ClientStart=new Client12_4_2();
  }
}
