import java.net.*;
import java.io.*;
import java.util.*;

public class Server15 {
  static int port;
  int messagein, flag, i;

  public Server15() {
     try{
         ServerSocket SS = new ServerSocket(port);
         System.out.println("Server is created and waiting Client to connect...");

         Socket socket = SS.accept();
         System.out.println("Client IP = " +
                             socket.getInetAddress().getHostAddress());

         DataInputStream instream = new DataInputStream(socket.getInputStream());
         DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
 
         InputStreamReader isr = new InputStreamReader(System.in);
         BufferedReader br = new BufferedReader(isr);
         System.out.println("Input data on keyboard...");

         while(true) {
            messagein = instream.readInt();
            System.out.print((char)messagein);
            if (messagein == 64) {
               flag = 1;
               System.out.println(" ");
            }

            while(flag == 1) {
               i = br.read();
               outstream.writeInt(i);
               if (i == 64) {
                  flag = 0;
               }
            }
         }
      }
      catch(IOException e){
         System.out.println(e.getMessage());
      }		
  }

  public static void main(String args[]){
         if(args.length < 1){
            System.out.println("Usage: java Server15 [port]");
            System.exit(1);
         }
         port=Integer.parseInt(args[0]);
         Server15 ServerStart=new Server15();
  }
}
