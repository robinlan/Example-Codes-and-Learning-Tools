import java.io.*;

class Ex08_8_2 {
   public static void main(String [] args)throws IOException {
      int i;

      if (args.length < 1) {
         System.out.println("Usage: java Ex12_8_2 [outfileName]");
         System.exit(1);
      }
      String outfileName = args[0];
 
      InputStreamReader isr =new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
 
      FileOutputStream fos = new FileOutputStream(outfileName);
       
      System.out.println("Input data in the keyboard: ");
      while(true) {
          i = br.read();
          fos.write(i);
      }
   }
}	
