import java.io.*;

class SampleP4
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入圓周率的值：");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));
      
      String str = br.readLine();

      double pi = Double.parseDouble(str);

      System.out.println("圓周率的值是" + pi + "。");
   }
}
