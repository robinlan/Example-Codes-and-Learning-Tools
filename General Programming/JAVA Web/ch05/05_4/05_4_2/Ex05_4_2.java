class A {
      A() {
           System.out.println("A's constructor");}
}

class B extends A {
      B() {
           System.out.println("B's constructor");}
}

class C extends B {
      C() {
           System.out.println("C's constructor");}
}

class Ex05_4_2 {
      public static void main(String[] args) {
             C c = new C();
      }
}
