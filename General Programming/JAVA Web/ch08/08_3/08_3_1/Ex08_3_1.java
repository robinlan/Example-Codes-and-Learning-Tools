import java.io.File;

class Ex08_3_1 {
    public static void main(String args[]) { 
        if (args.length < 1) {
	    System.out.println("Usage: java Ex12_3_1 [fileName]");
	    System.exit(1);
	}
	  
        String fileName = args[0];
        File f = new File(fileName); 
     
        System.out.println("The File is: ");
        System.out.println("  File Name: " + f.getName());
    }
}
