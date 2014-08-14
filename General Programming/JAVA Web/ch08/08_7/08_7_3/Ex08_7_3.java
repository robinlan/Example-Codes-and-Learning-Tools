import java.io.*;

class Ex08_7_3 {
   public static void main(String args[]) throws Exception  {
      int i;

      if (args.length < 2) {
         System.out.println("Usage: java Ex12_7_3 [infileName] [outfileName]");
         System.exit(1);
      }
	  
      String infileName = args[0];
      String outfileName = args[1];

      FileReader fr=new FileReader(infileName);
      FileWriter fw=new FileWriter(outfileName);

      while((i=fr.read()) !=-1) {
         fw.write(i); 
      }
      fw.flush();
   }
}   