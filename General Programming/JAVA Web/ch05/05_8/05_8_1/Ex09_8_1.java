class myClass1 {
      public void f() {System.out.println("In myClass1");
      }
}

class myClass2 extends myClass1 {
      public void f() {System.out.println("In myClass2");
	}
}

class Ex09_8_1 {
      public static void main(String[] args) {
             myClass1 a = new myClass2();
             a.f();
      }
}
