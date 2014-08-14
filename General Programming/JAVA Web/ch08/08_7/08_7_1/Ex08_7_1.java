import java.io.*;

class Ex08_7_1 {
   public static void main(String args[]) throws Exception {
      int i;
	
      if (args.length < 1) {
         System.out.println("Usage: java Ex12_7_1 [fileName]");
         System.exit(1);
      }
	  
      String fileName = args[0];
      FileReader fr=new FileReader(fileName);
      while((i=fr.read()) !=-1) {
          System.out.print((char)i);
      }
	
    }
}   