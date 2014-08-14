import java.io.*;

class SampleP5
{
   public static void main(String[] args) throws IOException
   {
      System.out.println("請輸入大於2的整數。");

      BufferedReader br =
       new BufferedReader(new InputStreamReader(System.in));

      String str = br.readLine();
      int num = Integer.parseInt(str);

       for(int i=2; i<=num; i++){
          if(i == num){
             System.out.println(num + "是質數。");
           }
           else if(num % i == 0){
             System.out.println(num + "不是質數。");
             break;
           }
       }
   }
}