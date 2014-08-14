interface myITF1 {public void f();}

interface myITF2 {public void g();}

class A implements myITF1, myITF2 {
      public void f() {System.out.println("In myITF1");}
      public void g() {System.out.println("In myITF2");}
}

class Ex05_7_3 {
      public static void main(String[] args) {
             A a = new A();
             a.f();
             a.g();
      }
}
