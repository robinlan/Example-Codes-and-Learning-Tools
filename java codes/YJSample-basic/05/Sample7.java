import java.io.*;

class Sample7
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請問你是男生嗎？");
      System.out.println("請輸入Y或N。");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();
      char res = str.charAt(0);

      if(res == 'Y' || res == 'y'){
         System.out.println("你是男生啊！");
      }
      else if(res == 'N' || res == 'n'){
         System.out.println("妳是女生啊！");
      }
      else{
         System.out.println("請輸入Y或N。");
      }
   }
}
