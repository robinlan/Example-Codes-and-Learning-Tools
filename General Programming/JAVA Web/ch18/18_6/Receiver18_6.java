import java.io.*;
import java.net.*;

public class Receiver18_6 {
  int messagein;
  static String iaddr;
  static int port;
  
  public Receiver18_6() {
     try{
         Socket socket=new Socket(InetAddress.getByName(iaddr),port);
         DataInputStream instream = new DataInputStream(socket.getInputStream());
 
         while(true) {
             messagein = instream.readInt();
             System.out.print((char)messagein);
         }
      }

      catch(IOException e){
         System.out.println(e.getMessage());
      }
  }

  public static void main(String args[]) {
      if (args.length < 2){
         System.out.println("USAGE: java Receiver18_6 [iaddr] [port]");
         System.exit(1);
      }

      iaddr = args[0];
      port=Integer.parseInt(args[1]);
      Receiver18_6 ReceiverStart=new Receiver18_6();
  }
}
