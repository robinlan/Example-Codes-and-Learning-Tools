import java.io.*;

class Ex08_8_3 {
   public static void main(String [] args)throws IOException {
      int i;

      if (args.length < 1) {
         System.out.println("Usage: java Ex12_8_3 [outfileName]");
         System.exit(1);
      }
      String outfileName = args[0];
 
      InputStreamReader isr =new InputStreamReader(System.in);
      BufferedReader br = new BufferedReader(isr);
 
      FileOutputStream fos = new FileOutputStream(outfileName);
      FileWriter fw=new FileWriter(outfileName);

      System.out.println("Input data in the keyboard: ");
      while((i=br.read()) !=-1) {
         fw.write(i); 
      }
      fw.flush();       
   }
}	
