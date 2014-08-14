import java.io.*;

class SampleP5
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入身高和體重：");

      BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));
      
      String str1 = br.readLine();
      String str2 = br.readLine();

      double num1 = Double.parseDouble(str1);
      double num2 = Double.parseDouble(str2);

      System.out.println("身高是" + num1 + "。");
      System.out.println("體重是" + num2 + "。");
   }
}
