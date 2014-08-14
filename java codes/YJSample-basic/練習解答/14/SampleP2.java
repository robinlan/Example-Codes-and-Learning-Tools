import java.io.*;

class SampleP2
{
   public static void main(String[] args)
   {
      try{
         PrintWriter pw = new PrintWriter
         (new BufferedWriter(new FileWriter("test1.txt")));
         
         pw.println("A long time ago,");
         pw.println("There was a little girl.");

         pw.close();
      }
      catch(IOException e){
         System.out.println("¿é¥X¤J¿ù»~¡C");
      }
   }
}