import java.net.*;
import java.io.*;

public class Server11_5 {
  static int port;

  public Server11_5() {
     try{
         ServerSocket SS = new ServerSocket(port);
         System.out.println("Server is created and waiting Client to connect...");
			
         Socket socket = SS.accept();
         System.out.println("connected from Client " +
                             socket.getInetAddress().getHostAddress());
         socket.close();
      }
      catch(IOException e){
         System.out.println(e.getMessage());
      }		
  }

  public static void main(String args[]){
         if(args.length < 1){
            System.out.println("Usage: java Server11_5 [port]");
            System.exit(1);
         }
         port=Integer.parseInt(args[0]);
         Server11_5 ServerStart=new Server11_5();
  }
}
