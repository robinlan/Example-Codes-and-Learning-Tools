class myException extends Exception {
   myException() {
        super("myException Message");
   }
}

class Ex06_4_2_1 {
   public static void main (String args[]) {
        int x, y;

        try {
           x = 10;
           y = 0;
           if(y==0) throw new myException();
           x = 10 / 0; 
           System.out.println("x= "+ x);
         }

        catch (myException e){
           System.out.println("In myException: "+e.getMessage());
        }
    }
}