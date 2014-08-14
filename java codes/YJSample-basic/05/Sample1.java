import java.io.*;

class Sample1
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入整數。");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();
      int res = Integer.parseInt(str);

      if(res == 1)
         System.out.println("輸入的是1。");

      System.out.println("結束處理。");
   }
}
