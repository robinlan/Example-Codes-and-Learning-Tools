class myException extends Exception {
   myException() {
        super("myException Message");
   }
}

class Ex06_4_2_2 {
   public static void main (String args[]) {
        myException f = new myException();
    
        int x, y;

        try {
           x = 10;
           y = 0;
           x = 10 / 0; 
           System.out.println("x= "+ x);
         }

        catch (ArithmeticException e){
           System.out.println("In Built Message: "+e.getMessage());
           System.out.println("In myException Message: "+f.getMessage());
        }
    }
}