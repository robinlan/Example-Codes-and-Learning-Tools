import java.io.*;

class Sample4
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請問要求從1到那個數字為止的和呢?");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();
      int num = Integer.parseInt(str);

      int sum = 0;
      for(int i=1; i<=num; i++){
         sum += i;
      }

      System.out.println("從1到" + num + "的和為" + sum + "。");
   }
}
