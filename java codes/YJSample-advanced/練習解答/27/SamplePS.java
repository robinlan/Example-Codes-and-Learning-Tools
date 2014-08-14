import java.io.*;
import java.net.*;

public class SamplePS
{
   public static void main(String[] args)
   {
      SamplePS sm = new SamplePS();

      if(args.length != 1){
         System.out.println("把计计秖ぃ癸");
         System.exit(1);
      }

      try{
         ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));

         System.out.println("タ单");
         while(true){
            Socket sc = ss.accept();
            System.out.println("舧");
                   
            PrintWriter pw = new PrintWriter
               (new BufferedWriter
               (new OutputStreamWriter(sc.getOutputStream())));
            pw.println("硂柑琌狝竟");
            pw.flush();
            pw.close();

            sc.close();
         }
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }
}