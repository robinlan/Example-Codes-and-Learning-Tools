import java.io.*;

class Sample9
{
   public static void main(String[] args) throws IOException
   {
      BufferedReader br =
                new BufferedReader(new InputStreamReader(System.in));

      int[] test = new int[5];
      System.out.println("請輸入" + test.length + "個人的分數:");

      for(int i=0; i<test.length; i++){
         String str = br.readLine();
         test[i] = Integer.parseInt(str);
      }

      for(int s=0; s<test.length-1; s++){
         for(int t=s+1; t<test.length; t++){
            if(test[t] > test[s]){
               int tmp = test[t];
               test[t] = test[s];
               test[s] = tmp;
            }
         }
      }

      for(int j=0; j<test.length; j++){
         System.out.println("第" + (j+1) + "名的分數是" + test[j] + "分。");
      }
   }
}
