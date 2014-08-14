import java.io.*;

class SampleP4
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入兩個整數。");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str1 = br.readLine();
      String str2 = br.readLine();

      int num1 = Integer.parseInt(str1);
      int num2 = Integer.parseInt(str2);

      int ans = Math.min(num1, num2);

      System.out.println(num1 + "和" + num2 + "之中較小的一方是" + ans + "。");
   }
}
