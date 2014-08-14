abstract class myNumber {
         private int number = 5;
         public int getNumber() { return number; }
}

class E extends myNumber {}

class Ex05_6_2 {
      public static void main(String[] args) {
             E a = new E();
             System.out.println("the number of a= "+a.getNumber());
      }
}
