import java.io.*;

class Ex08_6_2 {
   public static void main(String args[])throws Exception {
      if (args.length < 1) {
         System.out.println("Usage: java Ex12_6_2 [fileName]");
         System.exit(1);
      }
	  
      String fileName = args[0];
      FileOutputStream fos=new FileOutputStream(fileName);
      DataOutputStream dos = new DataOutputStream(fos);

      String line= "Test for DataOutputStream  ¤¤¤å´ú¸Õ";
      dos.write(line.getBytes());
      dos.close();
   }
}