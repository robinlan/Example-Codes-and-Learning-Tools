import java.io.*;
import java.net.*;

public class Client_upload {
  int i;
  static String iaddr;
  static int port;
  static String uploadFile;
  
  public Client_upload() {
     try{
         Socket cmdsocket=new Socket(InetAddress.getByName(iaddr),port);
         Socket datasocket=new Socket(InetAddress.getByName(iaddr),port);

         DataOutputStream cmdoutstream = new DataOutputStream(cmdsocket.getOutputStream());
         cmdoutstream.writeUTF(uploadFile);

         DataOutputStream dataoutstream = new DataOutputStream(datasocket.getOutputStream());
         FileInputStream fis = new FileInputStream(uploadFile);
         while((i=fis.read()) !=-1)
              dataoutstream.writeInt(i);
         dataoutstream.writeInt(i);

         System.out.println("File is sent to internet successfully!");
         cmdsocket.close();
         datasocket.close(); 
      }

      catch(IOException e){
          System.out.println(e.getMessage());
      }
  }

  public static void main(String args[]) {
      if (args.length < 3){
         System.out.println("USAGE: java Client_upload [iaddr] [port] [uploadFile]");	
         System.exit(1);
      }

      iaddr = args[0];
      port=Integer.parseInt(args[1]);
      uploadFile = args[2];
      Client_upload ClientStart=new Client_upload();
  }
}
