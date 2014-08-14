import java.io.*;

class Sample8
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請問要選哪條路線呢？");
      System.out.println("請輸入整數。");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();
      int res = Integer.parseInt(str);

      char ans = (res == 1) ? 'A' : 'B';

      System.out.println("選擇了" + ans + " 路線。");
   }
}
