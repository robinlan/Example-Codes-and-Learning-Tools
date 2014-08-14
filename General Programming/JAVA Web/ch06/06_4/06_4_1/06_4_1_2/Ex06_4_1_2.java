
class Ex06_4_1_2 {
   public static void main (String args[]) {
        ArithmeticException f = new ArithmeticException("DIY Message");
    
        int x, y;

        try {
           x = 10;
           y = 0;
           x = 10 / 0; 
           System.out.println("x= "+ x);
         }

        catch (ArithmeticException e){
           System.out.println("In Built Message: "+e.getMessage());
           System.out.println("In DIY Message: "+f.getMessage());
        }
    }
}