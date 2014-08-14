import java.io.*;

class SampleP2
{
   public static void main(String[] args)throws IOException
   {
      System.out.println("請輸入考試的成績。(輸入0就結束)");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      int num = 0; 
      int sum = 0;

      do{
         String str = br.readLine();
         num = Integer.parseInt(str);
         sum += num;
      }while(num != 0);

      System.out.println("總分為" + sum + "分。");
   }
}
