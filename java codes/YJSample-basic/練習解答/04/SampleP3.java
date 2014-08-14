import java.io.*;

class SampleP3
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入正方形的邊長：");

      BufferedReader br =
       new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();

      int width = Integer.parseInt(str);

      System.out.println("正方形的面積是" + (width*width) +"。");
   }
}