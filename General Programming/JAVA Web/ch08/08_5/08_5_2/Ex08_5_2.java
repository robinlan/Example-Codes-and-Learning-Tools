
import java.io.*;

class Ex08_5_2 {
   public static void main(String args[])throws Exception {
      if (args.length < 1) {
         System.out.println("Usage: java Ex12_5_2 [fileName]");
         System.exit(1);
      }
	  
      String fileName = args[0];
      FileOutputStream fos=new FileOutputStream(fileName);

      String line = "Test for FileOutputStream ¤¤¤å´ú¸Õ";

      fos.write(line.getBytes());
      fos.close();
   }
}   