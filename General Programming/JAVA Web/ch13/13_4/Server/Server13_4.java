import java.net.*;
import java.io.*;

public class Server13_4 {
  int messagein;
  static int port;
  static String outfilename;

  public Server13_4() {
     try{
         ServerSocket SS = new ServerSocket(port);
         System.out.println("Server is created and waiting Client to connect...");

         Socket socket = SS.accept();
         System.out.println("Client IP = " +
                             socket.getInetAddress().getHostAddress());

         DataInputStream instream = new DataInputStream(socket.getInputStream());
         FileOutputStream fos = new FileOutputStream(outfilename);

         while(messagein != -1){
              messagein = instream.readInt();     
              fos.write(messagein);
          }
         System.out.println("Data written to File successfully!");
      }
      catch(IOException e){
          System.out.println(e.getMessage());
      }		
  }

  public static void main(String args[]){
         if(args.length < 2){
            System.out.println("Usage: java Server13_4 [port] [outfilename]");
            System.exit(1);
         }
         port=Integer.parseInt(args[0]);
         outfilename = args[1];
         Server13_4 ServerStart=new Server13_4();
  }
}
