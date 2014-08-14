import java.io.*;
import java.net.*;

public class Client15 {
  int i, flag, messagein;
  static String iaddr;
  static int port;
  
  public Client15() {
     try{
         Socket socket=new Socket(InetAddress.getByName(iaddr),port);
         DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
         DataInputStream instream = new DataInputStream(socket.getInputStream());
 
         InputStreamReader isr = new InputStreamReader(System.in);
         BufferedReader br = new BufferedReader(isr);
         System.out.println("Input data on keyboard...");

         while(true) {
             i = br.read();
             outstream.writeInt(i);
             if (i == 64) {
                flag = 1;
             }

             while (flag == 1) {
               messagein = instream.readInt();
               System.out.print((char)messagein);
               if (messagein == 64) {
                  flag = 0;
               System.out.println(" ");
               }
             }
         }
      }

      catch(IOException e){
         System.out.println(e.getMessage());
      }
  }

  public static void main(String args[]) {
      if (args.length < 2){
         System.out.println("USAGE: java Client15 [iaddr] [port]");
         System.exit(1);
      }

      iaddr = args[0];
      port = Integer.parseInt(args[1]);
      Client15 ClientStart=new Client15();
  }
}
