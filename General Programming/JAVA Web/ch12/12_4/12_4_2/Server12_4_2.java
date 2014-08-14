import java.net.*;
import java.io.*;
import java.util.*;

public class Server12_4_2 {
  static int  port;
  static String messageout;
  static String messagein;


  public Server12_4_2() {
     try{
         ServerSocket SS = new ServerSocket(port);
         System.out.println("Server is created and waiting Client to connect...");

         Date currentDate = new Date();
			
         while(true){
              Socket socket = SS.accept();
              System.out.println("Client IP = " +
                                  socket.getInetAddress().getHostAddress());
              System.out.println("date = " + currentDate);

              DataInputStream instream = new DataInputStream(socket.getInputStream());
              messagein = instream.readUTF();
              System.out.println("messagein = " + messagein);

              System.out.println("waiting...");

              DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
              outstream.writeUTF(messageout);

          }
      }
      catch(IOException e){
         System.out.println(e.getMessage());
      }		
  }

  public static void main(String args[]){
       if(args.length < 2){
            System.out.println("Usage: java Server12_4_2 [port] [messageout]");
            System.exit(1);
       }
       port=Integer.parseInt(args[0]);
       messageout = args[1];
       Server12_4_2 ServerStart=new Server12_4_2();
  }
}
