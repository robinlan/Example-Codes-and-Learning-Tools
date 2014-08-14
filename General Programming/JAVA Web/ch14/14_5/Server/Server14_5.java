import java.net.*;
import java.io.*;

public class Server14_5 {
  static int port;
  static String outfilename;
  int messagein;

  public Server14_5() {
     try{
         ServerSocket SS = new ServerSocket(port);
         System.out.println("Server is created and waiting Client to connect...");

         Socket socket = SS.accept();
         System.out.println("Client IP = " +
                             socket.getInetAddress().getHostAddress());

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
         if(args.length < 2){
            System.out.println("Usage: java Server14_5 [port] [outfilename]");
            System.exit(1);
         }
         port=Integer.parseInt(args[0]);
         outfilename = args[1];
         Server14_5 ServerStart=new Server14_5();
  }
}
