import java.io.*;

public class Sample2
{
   public static void main(String[] args)
   {
      if(args.length != 2){
         System.out.println("參數的數量不對。");
         System.exit(1);
      }

      try{
         File fl1 = new File(args[0]);
         File fl2 = new File(args[1]);

         System.out.println("變更前的檔案名稱為" + fl1.getName() + "。");

         boolean res = fl1.renameTo(fl2);

         if(res == true){
            System.out.println("檔案名稱變更了。");
            System.out.println("變更後的檔案名稱為" + fl2.getName() + "。");
         }
         else{
            System.out.println("沒辦法變更檔案名稱。");
         }
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }
}