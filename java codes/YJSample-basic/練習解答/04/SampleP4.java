import java.io.*;

class SampleP4
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入三角形的底和高：");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str1 = br.readLine();
      String str2 = br.readLine();

      double height = Integer.parseInt(str1);
      double width = Integer.parseInt(str2);

      System.out.println("三角形的面積是" + (height * width / (double) 2) + "。");
   }
}
