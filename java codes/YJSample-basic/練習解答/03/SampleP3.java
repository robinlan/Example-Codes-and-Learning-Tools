import java.io.*;

class SampleP3
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("你今年幾歲？");

      BufferedReader br =
       new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();

      int num = Integer.parseInt(str);

      System.out.println("你今年" + num + "歲。");
   }
}
