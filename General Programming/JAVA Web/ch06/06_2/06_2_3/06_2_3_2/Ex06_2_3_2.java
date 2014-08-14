
class Ex06_2_3_2 {
   public static void main (String args[]) {
        int x;
        try {
           x = 10 / 0; 
           System.out.println("x= "+ x);
          }
         finally {
           System.out.println("In finally");
          }
       }
}