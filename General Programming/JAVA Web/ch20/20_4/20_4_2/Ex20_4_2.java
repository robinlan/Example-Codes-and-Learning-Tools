import java.util.*;

class Ex20_4_2 {
   public Ex20_4_2() {
      Vector vec = new Vector();

      vec.addElement("Here is No.0 Line");
      vec.addElement("Here is No.1 Line");
      vec.addElement("Here is No.2 Line");

      System.out.println("isEmpty() : " + vec.isEmpty());
      System.out.println("elementAt(1) : " + vec.elementAt(1));
      System.out.println("firstElement(0) : " + vec.firstElement());
      System.out.println("lastElement(0) : " + vec.lastElement());

      vec.insertElementAt("Here is the insert Object", 1);
      System.out.println("elementAt(1) : " + vec.elementAt(1));
   }

    public static void main(String[] args) {
      Ex20_4_2 teststart = new Ex20_4_2();
    }
}
