import java.net.*;
import java.io.*;

class Server11_3 {
   public static void main(String args[]) {
      try {
        
        InetAddress iaddr = InetAddress.getByName("163.15.40.242");
        System.out.println("iaddr.getHostAddress() = " + iaddr.getHostAddress());
        System.out.println("iaddr.getHostName() = " + iaddr.getHostName());
      }

      catch(UnknownHostException e) {
        System.out.println(e.getMessage());
      }
   }
 }









 



