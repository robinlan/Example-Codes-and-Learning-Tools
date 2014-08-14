
class Ex06_2_2_3 {
   public static void main (String args[])
       {
        int x;
        try
          {
           x = 10 / 0; 
           System.out.println("x= "+ x);
          }
        catch (Exception e) 
          {
           System.out.println("In Exception :"+e.getMessage());
          }
       }
}