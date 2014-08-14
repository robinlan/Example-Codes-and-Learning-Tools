interface myITF {public void f();}

class A implements myITF {
      public void f() {System.out.println("myITF");}
}

class Ex05_7_1 {
      public static void main(String[] args) {
             A a = new A();
             a.f();
      }
}
