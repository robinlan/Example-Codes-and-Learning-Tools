class MyNumber{
      private int number;
      public void setNumber(int i) {
             number = i;
      }
      public int getNumber() {
             return number;
      }
}

class E extends MyNumber {}  

class Ex05_2_1 {
      public static void main(String[] args) {
      E a = new E();
		
      a.setNumber(5);
      System.out.println("a=: "+a.getNumber());
      }
}
