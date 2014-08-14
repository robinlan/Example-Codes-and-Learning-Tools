import java.io.*;

class Sample10
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入成績評比(1～5)。");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();
      int res = Integer.parseInt(str);

      switch(res){
         case 1:
         case 2:
            System.out.println("還要再加強唷！");
            break;
         case 3:
         case 4:
            System.out.println("就這樣保持下去吧。");
            break;
         case 5:
            System.out.println("你太優秀了。");
            break;
         default:
            System.out.println("請輸入1～5的成績評比。");
            break;
      }
   }
}
