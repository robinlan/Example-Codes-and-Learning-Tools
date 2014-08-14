import java.io.*;

class Sample6
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入a或b。");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();
      char res = str.charAt(0);

      switch(res){
         case 'a':
            System.out.println("輸入的是a。");
            break;
         case 'b':
            System.out.println("輸入的是b。");
            break;
         default:
            System.out.println("請輸入a或b。");
            break;
      }
   }
}
