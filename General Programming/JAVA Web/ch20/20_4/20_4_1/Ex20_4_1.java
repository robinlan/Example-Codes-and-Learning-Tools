import java.util.*;

class Ex20_4_1 {
   public Ex20_4_1() {
      Vector vec1 = new Vector(0);
      System.out.println("vec1 Initial Capacity = " + vec1.capacity());

      vec1.addElement("Here is No.0 Line");
      vec1.addElement("Here is No.1 Line");
      vec1.addElement("Here is No.2 Line");
      System.out.println("vec1 Adjusted Capacity = " + vec1.capacity());
    
      Vector vec2 = new Vector(0, 50);
      System.out.println("vec2 Initial Capacity = " + vec2.capacity());

      vec2.addElement("Here is No.0 Line");
      vec2.addElement("Here is No.1 Line");
      vec2.addElement("Here is No.2 Line");
      System.out.println("vec2 Adjusted Capacity = " + vec2.capacity());     
   }

    public static void main(String[] args) {
      Ex20_4_1 teststart = new Ex20_4_1();
    }
}
