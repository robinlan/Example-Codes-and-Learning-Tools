interface myITF {public void f();}

class myNumber {
      public void g() {System.out.println("In myNumber");}
}

class A extends myNumber implements myITF {
      public void f() {System.out.println("In myITF");}

}

class Ex05_7_2 {
      public static void main(String[] args) {
             A a = new A();
             a.f();
             a.g();
      }
}
