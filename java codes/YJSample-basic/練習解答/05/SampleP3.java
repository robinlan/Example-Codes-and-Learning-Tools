import java.io.*;

class SampleP3
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入兩個整數。");

      BufferedReader br =
        new BufferedReader(new InputStreamReader(System.in));

      String str1 = br.readLine();
      String str2 = br.readLine();

      int num1 = Integer.parseInt(str1);
      int num2 = Integer.parseInt(str2);

      if(num1 < num2){
         System.out.println(num1 + "比" + num2 + "來得小。");
      }
      else if(num1 > num2){
         System.out.println(num1 + "比" + num2 + "來得大。");
      }
      else{
         System.out.println("兩個數值是相同的。");
      }
   }
}
