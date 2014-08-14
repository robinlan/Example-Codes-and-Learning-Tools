import java.io.*;

class Sample4
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入字串。");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str1 = br.readLine();

      System.out.println("請輸入要新增的字串。");

      String str2 = br.readLine();

      StringBuffer sb = new StringBuffer(str1);
      sb.append(str2);
      
      System.out.println("在「" + str1 + "」後新增「" + str2 + "」的話，就會變成「" + sb + "」。");
   }
}
