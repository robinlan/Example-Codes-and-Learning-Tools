import java.io.*;

class Ex08_6_3 {
   public static void main(String args[])throws Exception {
     int i;
   
     if (args.length < 2) {
        System.out.println("Usage: java Ex12_6_3 [infileName] [outfileName]");
        System.exit(1);
     }
	  
     String infileName = args[0];
     String outfileName = args[1];

     FileInputStream fis=new FileInputStream(infileName);
     DataInputStream dis = new DataInputStream(fis);

     FileOutputStream fos=new FileOutputStream(outfileName);
     DataOutputStream dos = new DataOutputStream(fos);

     while((i=dis.read()) !=-1) {
          dos.write(i);
     }
   }
}

