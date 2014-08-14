import java.io.*;

class SampleP5
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入科目1~5的分數：");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str1 = br.readLine();
      String str2 = br.readLine();
      String str3 = br.readLine();
      String str4 = br.readLine();
      String str5 = br.readLine();

      int sum = 0;
      sum += Integer.parseInt(str1);
      sum += Integer.parseInt(str2);
      sum += Integer.parseInt(str3);
      sum += Integer.parseInt(str4);
      sum += Integer.parseInt(str5);

      System.out.println("5個科目的總分為" + sum + "。");
      System.out.println("5個科目的平均為" + (sum /(double) 5) + "。");
   }
}
