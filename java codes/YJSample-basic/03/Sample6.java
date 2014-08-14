import java.io.*;

class Sample6
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

      System.out.println("第一個輸入的是" + num1 + "。");
      System.out.println("第二個輸入的是" + num2 + "。");
   }
}
