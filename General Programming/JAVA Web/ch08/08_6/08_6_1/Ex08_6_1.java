import java.io.*;

class Ex08_6_1 {
   public static void main(String args[])throws Exception {
     int i;
   
     if (args.length < 1) {
        System.out.println("Usage: java Ex12_6_1 [fileName]");
        System.exit(1);
     }
	  
     String fileName = args[0];
     FileInputStream fis=new FileInputStream(fileName);
     DataInputStream dis = new DataInputStream(fis);

     while((i=dis.read()) !=-1) {
          System.out.print((char)i);
     }
   }
}

