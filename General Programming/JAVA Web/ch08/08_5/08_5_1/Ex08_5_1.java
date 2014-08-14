
import java.io.*;

class Ex08_5_1 {
   public static void main(String args[]) throws Exception {
      int i;
	
      if (args.length < 1) {
	 System.out.println("Usage: java Ex12_5_1 [fileName]");
	 System.exit(1);
      }

      String fileName = args[0];	  
      FileInputStream fis=new FileInputStream(fileName);

      while((i=fis.read()) !=-1) {
          System.out.print((char)i);
      }
   }
}   