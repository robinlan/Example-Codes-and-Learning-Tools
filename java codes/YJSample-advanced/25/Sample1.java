import java.io.*;

public class Sample1
{
   public static void main(String[] args)
   {
      if(args.length != 1){
         System.out.println("參數的數量不對。");
         System.exit(1);
      }

      try{
         File fl = new File(args[0]);
         System.out.println("檔案名稱為" + fl.getName() + "。");
         System.out.println("絕對路徑為" + fl.getAbsolutePath() + "。");
         System.out.println("檔案大小為" + fl.length() + "位元。");
      }   
      catch(Exception e){
         e.printStackTrace();
      }
   }
}