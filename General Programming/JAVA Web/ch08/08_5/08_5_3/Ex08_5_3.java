
import java.io.*;

class Ex08_5_3 {
   public static void main(String args[]) throws Exception {
      int i;
	
      if (args.length < 2) {
	 System.out.println("Usage: java Ex12_5_3 [infileName] [outfileName]");
	 System.exit(1);
      }

      String infileName = args[0];
      String outfileName = args[1];

      FileInputStream fis=new FileInputStream(infileName);
      FileOutputStream fos=new FileOutputStream(outfileName);

      while((i=fis.read()) !=-1) {
          fos.write(i);
      }
   }
}   