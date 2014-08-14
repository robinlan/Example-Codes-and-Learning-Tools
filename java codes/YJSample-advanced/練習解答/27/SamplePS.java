import java.io.*;
import java.net.*;

public class SamplePS
{
   public static void main(String[] args)
   {
      SamplePS sm = new SamplePS();

      if(args.length != 1){
         System.out.println("參數的數量不對。");
         System.exit(1);
      }

      try{
         ServerSocket ss = new ServerSocket(Integer.parseInt(args[0]));

         System.out.println("正在等待。");
         while(true){
            Socket sc = ss.accept();
            System.out.println("歡迎。");
                   
            PrintWriter pw = new PrintWriter
               (new BufferedWriter
               (new OutputStreamWriter(sc.getOutputStream())));
            pw.println("這裡是伺服器。");
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