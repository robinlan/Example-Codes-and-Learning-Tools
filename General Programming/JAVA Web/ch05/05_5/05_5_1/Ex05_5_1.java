final class myNumber {
      int number= 5;
}

class E extends myNumber{
      int number= 10;
      public int getsuperNumber() {return super.number;}
      public int getthisNumber() {return this.number;}
}

class Ex05_5_1 {
      public static void main(String[] args) {
             E a = new E();
 
             System.out.println("The myNumber_number of a is: "
                                 +a.getsuperNumber());
             System.out.println("The E_number of a is: "+a.getthisNumber());
      }
}
