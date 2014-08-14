import java.io.*;
import java.net.*;

public class Sample4S
{
   public static final int PORT = 10000;

   public static void main(String[] args)
   {
      Sample4S sm = new Sample4S();

      try{
         ServerSocket ss = new ServerSocket(PORT);

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