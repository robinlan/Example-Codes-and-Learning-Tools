import java.io.*;

class SampleP2
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入整數。");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();
      int res = Integer.parseInt(str);

      if((res % 2) == 0)
         System.out.println(res + "是偶數。");
      else
         System.out.println(res + "是奇數。");
   }
}
