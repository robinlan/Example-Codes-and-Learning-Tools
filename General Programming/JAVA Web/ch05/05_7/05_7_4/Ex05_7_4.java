interface myNumber {
      public int number= 5;
      public void setprint();
      public int getNumber(); 
}

class E implements myNumber {
      public void setprint() {System.out.println("In E");}
      public int getNumber() {return number;}
}

class Ex05_7_4 {
      public static void main(String[] args) {
             E a = new E();
             a.setprint();
             System.out.println("the number of a= "+a.getNumber());
      }
}
