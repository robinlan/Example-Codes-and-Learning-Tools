
class Ex06_4_1_1 {
   public static void main (String args[]) {
        int x, y;

        try {
           x = 10;
           y = 0;
           if(y==0) throw new ArithmeticException("DIY Message");
           x = 10 / 0; 
           System.out.println("x= "+ x);
         }

        catch (ArithmeticException e){
           System.out.println("In ArithmeticException:"+e.getMessage());
        }
    }
}