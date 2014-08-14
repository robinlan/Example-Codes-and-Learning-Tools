
class Ex06_2_3_1 {
   public static void main (String args[]) {
        int x;

        try {
           x = 10 / 0; 
           System.out.println("x= "+ x);
        }

        catch (Exception e) {
           System.out.println("In Exception :"+e.getMessage());
        }

        finally {
           System.out.println("In finally");
        }
    }
}