abstract class myNumber {
         public int number = 5 ;
         public abstract int getNumber() {return number;} 
}

class E extends myNumber {}

class Ex05_6_3 {
      public static void main(String[] args) {
             E a = new E();
             System.out.println("the number of a= "+a.getNumber());
      }
}
