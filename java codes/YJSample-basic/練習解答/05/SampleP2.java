import java.io.*;

class SampleP2
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("叫块俱计");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();
      int res = Integer.parseInt(str);

      if((res % 2) == 0)
         System.out.println(res + "琌案计");
      else
         System.out.println(res + "琌计");
   }
}
