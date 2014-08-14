import java.io.*;

class Sample5
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

      int ans = Math.max(num1, num2);

      System.out.println(num1 + "與" + num2 + "中較大的是" + ans + "。");
   }
}