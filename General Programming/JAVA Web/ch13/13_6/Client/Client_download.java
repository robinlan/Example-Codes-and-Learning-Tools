import java.io.*;
import java.net.*;

public class Client_download {
  int i;
  static String iaddr;
  static int port;
  static String downloadFile;
  int messagein;
  
  public Client_download() {
     try{
         Socket cmdsocket=new Socket(InetAddress.getByName(iaddr),port);
         Socket datasocket=new Socket(InetAddress.getByName(iaddr),port);

         DataOutputStream cmdoutstream = new DataOutputStream(cmdsocket.getOutputStream());
         cmdoutstream.writeUTF(downloadFile);

         DataInputStream datainstream = new DataInputStream(datasocket.getInputStream());
         FileOutputStream fos = new FileOutputStream(downloadFile);

         while(messagein != -1){
              messagein = datainstream.readInt();     
              fos.write(messagein);
          }
         System.out.println("File is downloaded successfully!");
         datasocket.close(); 
      }

      catch(IOException e){
          System.out.println(e.getMessage());
      }
  }

  public static void main(String args[]) {
      if (args.length < 3){
         System.out.println("USAGE: java Client_download [iaddr] [port] [downloadFile]");
         System.exit(1);
      }

      iaddr = args[0];
      port=Integer.parseInt(args[1]);
      downloadFile = args[2];
      Client_download ClientStart=new Client_download();
  }
}
