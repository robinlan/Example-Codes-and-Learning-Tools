import java.io.*;

class SampleP3
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入字串。");

      BufferedReader br =
       new BufferedReader(new InputStreamReader(System.in));

      String str1 = br.readLine();

      System.out.println("請以整數來輸入a的插入位置。");

      String str2 = br.readLine();
      int num = Integer.parseInt(str2);

      StringBuffer str3 = new StringBuffer(str1);
      str3.insert(num, 'a');

      System.out.println("字串變成" + str3 + "了。");
   }
}
