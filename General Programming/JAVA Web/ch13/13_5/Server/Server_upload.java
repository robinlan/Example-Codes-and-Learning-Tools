import java.net.*;
import java.io.*;

public class Server_upload {
  static int port;
  static String uploadFile;
  int messagein;

  public Server_upload() {
     try{
         ServerSocket SS = new ServerSocket(port);
         System.out.println("Server_upload is ready...");

         Socket cmdsocket = SS.accept();
         Socket datasocket = SS.accept();

         System.out.println("Client IP = " +
                             cmdsocket.getInetAddress().getHostAddress());
         DataInputStream cmdinstream = new DataInputStream(cmdsocket.getInputStream());
         uploadFile = cmdinstream.readUTF();

         DataInputStream datainstream = new DataInputStream(datasocket.getInputStream());
         FileOutputStream fos = new FileOutputStream(uploadFile);

         while(messagein != -1){
              messagein = datainstream.readInt();     
              fos.write(messagein);
          }
         System.out.println("File is uploaded successfully!");
      }
      catch(IOException e){
         System.out.println(e.getMessage());
      }		
  }

  public static void main(String args[]){
      if(args.length < 1){
         System.out.println("Usage: java Server_upload [port]");
         System.exit(1);
      }
      port = Integer.parseInt(args[0]);
      Server_upload ServerStart=new Server_upload();
  }
}
