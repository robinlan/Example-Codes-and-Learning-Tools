import java.io.*;
import java.net.*;

public class Speaker18_7 {
  int i;
  static String iaddr;
  static int port;
  DataOutputStream  outstream;
  
  public Speaker18_7() {
     try{
         Socket socket=new Socket(InetAddress.getByName(iaddr),port);
         DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
 
         InputStreamReader isr = new InputStreamReader(System.in);
         BufferedReader br = new BufferedReader(isr);
         System.out.println("Input data on keyboard...");

         while(true) {
               i = br.read();
               outstream.writeInt(i);
         }
      }

      catch(IOException e){
         System.out.println(e.getMessage());
      }
  }

  public static void main(String args[]) {
      if (args.length < 2){
         System.out.println("USAGE: java Speaker18_7 [iaddr] [port]");
         System.exit(1);
      }

      iaddr = args[0];
      port=Integer.parseInt(args[1]);
      Speaker18_7 SpeakerStart=new Speaker18_7();
  }
}
