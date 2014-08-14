import java.net.*;
import java.io.*;

public class Server14_4_1 {
  Socket  socket;
  static int port;
  int messagein;

  public Server14_4_1() {
     try{
         ServerSocket SS = new ServerSocket(port);
         System.out.println("Server is created and waiting Client to connect...");

         Socket socket = SS.accept();
         System.out.println("Client IP = " +
                             socket.getInetAddress().getHostAddress());
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

  public static void main(String args[]){
         if(args.length < 1){
            System.out.println("Usage: java Server14_4_1 [port]");
            System.exit(1);
         }
         port=Integer.parseInt(args[0]);
         Server14_4_1 ServerStart=new Server14_4_1();
  }
}
