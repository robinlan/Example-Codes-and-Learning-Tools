import java.io.*;

class Sample4
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入字串。");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();
      
      System.out.println("剛才輸入的字串是：" + str);
   }
}
