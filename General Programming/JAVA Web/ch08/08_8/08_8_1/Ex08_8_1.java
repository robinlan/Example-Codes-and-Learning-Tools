import java.io.*;

class Ex08_8_1 {
   public static void main(String [] args)throws IOException {
      int i;
 
      InputStreamReader isr = new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
       
      System.out.println("Input data in the keyboard: ");
      while(true) {
          i = br.read();
          System.out.print((char)i);
      }
   }
}	
