import java.io.*;
import java.net.*;

public class Client13_4 {
  int i;
  static String iaddr;
  static int port;
  static String infilename;
  
  public Client13_4() {
     try{
         Socket socket=new Socket(InetAddress.getByName(iaddr),port);
         DataOutputStream outstream = new DataOutputStream(socket.getOutputStream());
 
         FileInputStream fis = new FileInputStream(infilename);
         while((i=fis.read()) !=-1)
              outstream.writeInt(i);
         outstream.writeInt(i);

         System.out.println("Data sent to internet successfully!");
         socket.close(); 
      }

      catch(IOException e){
         System.out.println(e.getMessage()); 
      }
  }

  public static void main(String args[]) {
      if (args.length < 3){
         System.out.println("USAGE: java Client13_4 [iaddr] [port] [infilename]");	
         System.exit(1);
      }

      iaddr = args[0];
      port=Integer.parseInt(args[1]);
      infilename = args[2];
      Client13_4 ClientStart=new Client13_4();
  }
}
