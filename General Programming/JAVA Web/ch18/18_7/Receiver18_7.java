import java.net.*;
import java.io.*;

public class Receiver18_7 {
  static String iaddr;
  static int port;
  static String outfilename;
  int messagein;

  public Receiver18_7() {
     try{
         Socket socket=new Socket(InetAddress.getByName(iaddr),port);
         DataInputStream instream = new DataInputStream(socket.getInputStream());
         FileWriter fw = new FileWriter(outfilename);

         while(messagein != -1){
              messagein = instream.readInt();     
              fw.write(messagein);
          }
         fw.flush();
         System.out.println("Data written to File successfully!");
      }
      catch(IOException e){
         System.out.println(e.getMessage());
      }		
  }

  public static void main(String args[]){
         if(args.length < 3){
            System.out.println("Usage: java Receiver18_7 [iaddr] [port] [outfilename]");
            System.exit(1);
         }
         iaddr = args[0];
         port=Integer.parseInt(args[1]);
         outfilename = args[2];
         Receiver18_7 ReceiverStart=new Receiver18_7();
  }
}
