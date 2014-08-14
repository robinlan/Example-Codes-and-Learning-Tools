import java.net.*;
import java.io.*;

public class Server_download {
  static int port;
  static String downloadFile;
  int messagein, i;

  public Server_download() {
     try{
         ServerSocket SS = new ServerSocket(port);
         System.out.println("Server_download is ready...");

         Socket cmdsocket = SS.accept();
         Socket datasocket = SS.accept();

         System.out.println("Client IP = " +
                             cmdsocket.getInetAddress().getHostAddress());
         DataInputStream cmdinstream = new DataInputStream(cmdsocket.getInputStream());
         downloadFile = cmdinstream.readUTF();

         DataOutputStream dataoutstream = new DataOutputStream(datasocket.getOutputStream());
         FileInputStream fis = new FileInputStream(downloadFile);
         while((i=fis.read()) !=-1)
              dataoutstream.writeInt(i);
         dataoutstream.writeInt(i);

         System.out.println("File is sent to internet successfully!");
         datasocket.close(); 

      }
      catch(IOException e){
         System.out.println(e.getMessage());
      }		
  }

  public static void main(String args[]){
         if(args.length < 1){
            System.out.println("Usage: java Server_download [port]");
            System.exit(1);
         }
         port=Integer.parseInt(args[0]);
         Server_download ServerStart=new Server_download();
  }
}
